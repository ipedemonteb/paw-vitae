import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import {
    Mail,
    Phone,
    SquarePen,
    ShieldPlus,
    UserStar,
    Stethoscope,
    FileBadge,
    BadgeCheck,
    Calendar,
    Hospital,
    MapPin,
    BriefcaseBusiness, Trash2, Plus, Loader2, HeartPlus
} from "lucide-react";
import { Card, CardContent } from "@/components/ui/card.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";
import { RatingCard } from "@/components/Rating.tsx";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {
    useDoctorImageUrl,
    useUpdateDoctorCertificatesMutation,
    useUpdateDoctorExperienceMutation,
    useUpdateDoctorProfileMutation
} from "@/hooks/useDoctors.ts";
import {
    useDoctor,
    useDoctorBiography, useDoctorCertifications,
    useDoctorCoverages,
    useDoctorExperience,
    useDoctorSpecialties
} from "@/hooks/useDoctors.ts";
import type {
    DoctorDTO,
    ExperienceDTO,
    CertificationDTO,
    DoctorProfileDTO,
    ExperienceForm,
    CertificateForm
} from "@/data/doctors.ts";
import type { CoverageDTO } from "@/data/coverages.ts";
import type { OfficeDTO } from "@/data/offices.ts";
import GenericError from "@/pages/GenericError.tsx";
import { useRatings } from "@/hooks/useRatings.ts";
import type { SpecialtyDTO } from "@/data/specialties.ts";
import type { RatingsDTO } from "@/data/ratings.ts";
import { useNeighborhood } from "@/hooks/useNeighborhoods.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import React, { useEffect, useState } from "react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogTitle,
    DialogTrigger,
    DialogHeader
} from "@/components/ui/dialog.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";
import { toast } from "sonner";
import {DatePicker} from "@/components/ui/date-picker.tsx";
import {isoToLocalDate, localDateToIso} from "@/utils/dateUtils.ts";
import {useDoctorOffices} from "@/hooks/useOffices.ts";

const profileContainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6 mb-6";

function PublicProfile() {
    const auth = useAuth();
    const { id } = useParams<{ id: string }>();
    const { data: doctor, isLoading: isLoadingDoctor, isError } = useDoctor(id);
    const { data: specialties, isLoading: isLoadingSpecialties } = useDoctorSpecialties(doctor?.specialties);
    const { data: coverages, isLoading: isLoadingCoverages } = useDoctorCoverages(doctor?.coverages);
    const { data: experiences, isLoading: isLoadingExperiences } = useDoctorExperience(doctor?.experiences);
    const { data: certifications, isLoading: isLoadingCertifications } = useDoctorCertifications(doctor?.certifications);
    const { data: profile, isLoading: isLoadingProfile } = useDoctorBiography(doctor?.profile);
    const { data: ratings, isLoading: isLoadingRatings } = useRatings(doctor?.ratings);
    const { data: offices, isLoading: isLoadingOffices } = useDoctorOffices(doctor?.offices);

    if (isLoadingDoctor || isLoadingCertifications || isLoadingCoverages || isLoadingExperiences || isLoadingProfile || isLoadingOffices || isLoadingRatings || isLoadingSpecialties)
        return <div className="flex justify-center mt-36"><Loader2 className="animate-spin h-8 w-8" /></div>;

    if (isError || !doctor) return <GenericError code={404} />;

    const maxBadges = 4;
    const isOwner = auth.email === doctor?.email;

    return (
        <div className={profileContainer}>
            <ProfileCard doctor={doctor} profile={profile} specialties={specialties || []} maxBadges={maxBadges} isOwner={isOwner} />
            <AboutMeCard bio={profile?.bio || ""} />
            <CoverageCard coverages={coverages || []} />
            <OfficesCard offices={offices || []} />
            <ExperienceCard experiences={experiences || []} isOwner={isOwner} updateUrl={doctor.experiences} />
            <CertificatesCard certifications={certifications || []} isOwner={isOwner} updateUrl={doctor.certifications} />
            <RatingsCard ratings={ratings || []} />
        </div>
    );
}

const titleIcon = "w-5 h-5";
const avatarContainer = "flex items-center w-20 h-20 mx-6 mb-2 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";
const userDataContainer = "flex flex-col items-center sm:items-start";
const userName = "text-[var(--text-color)] text-xl font-[700] mb-1";
const dataContainer = "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData = "flex flex-row items-center gap-1";
const contactIcon = "w-4 h-4";
const ratingContent = "flex flex-row items-center gap-2 font-bold";
const ratingText = "font-medium text-sm text-[var(--text-light)]";
const card = "p-0 gap-0";
const cardTitle = "flex flex-wrap items-center gap-1 px-6 py-2 bg-[var(--primary-bg)] text-[var(--primary-dark)] rounded-t-xl";
const aboutContent = "px-7 pt-4 pb-6";
const cardTitleText = "text-lg font-[500] min-w-0";
const profileContent = "flex flex-col gap-0 items-center sm:flex-row pt-6";
const aboutTitle = " text-lg font-[500]";
const aboutText = " wrap-break-word flex-wrap text-[var(--text-color)] text-md";

function EmptySection({ icon: Icon, text }: { icon: React.ElementType, text: string }) {
    return (
        <div className="flex flex-col items-center justify-center w-full py-8 text-(--text-light) opacity-60">
            <Icon className="w-10 h-10 mb-3 stroke-1" />
            <p className="text-sm font-medium text-center">{text}</p>
        </div>
    );
}

function ProfileCard({ doctor, profile, specialties, maxBadges, isOwner }: {
    doctor: DoctorDTO;
    profile: DoctorProfileDTO | undefined;
    specialties: SpecialtyDTO[];
    maxBadges: number;
    isOwner: boolean;
}) {
    const avatarFallbackText = (() => {
        const a = doctor.name?.trim()?.[0] ?? "";
        const b = doctor.lastName?.trim()?.[0] ?? "";
        return (a + b).toUpperCase() || "U";
    })();
    const { t } = useTranslation();
    const { url: getDoctorImgUrl } = useDoctorImageUrl(String(doctor.self.split('/').pop()));
    const specialtyNames = specialties.map(s => t(s.name));
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <div className="flex justify-center items-center gap-1">
                    <Stethoscope className={titleIcon}></Stethoscope>
                    <h1 className={cardTitleText}>{t("doctor.profile.doctor")}</h1>
                </div>
                {isOwner && (
                    <EditProfileDialog
                        doctorUrl={doctor.profile}
                        initialBio={profile?.bio || ""}
                        initialDescription={profile?.description || ""}
                        trigger={
                            <Button className={editComponentButton}>
                                <SquarePen className="w-4 h-4 mr-2" />
                                {t("doctor.profile.card.edit")}
                            </Button>
                        }
                    />
                )}
            </div>
            <div className={profileContent}>
                <Avatar className={avatarContainer}>
                    <AvatarImage src={getDoctorImgUrl || undefined} />
                    <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                </Avatar>
                <div className={userDataContainer}>
                    <h1 className={userName}>{doctor.name} {doctor.lastName}</h1>
                    <div className={dataContainer}>
                        <div className={contactData}>
                            <Mail className={contactIcon} />
                            <p className="max-w-37.5 truncate sm:max-w-75 sm:truncate">{doctor.email}</p>
                        </div>
                        <div className={contactData}>
                            <Phone className={contactIcon} />
                            <p>{doctor.phone}</p>
                        </div>
                    </div>
                    {doctor.ratingCount > 0 ? (
                        <div className={ratingContent}>
                            <RatingStars rating={doctor.rating} sizeClassName="h-4 w-4" />
                            <p>{doctor.rating}</p>
                            <p className={ratingText}>({doctor.ratingCount} {t("doctor.profile.card.rating")})</p>
                        </div>
                    ) : null}
                    <BadgeComponent specialties={specialtyNames} maxBadges={maxBadges} />
                </div>
            </div>
            <div className={aboutContent}>
                <h1 className={aboutTitle}>{t("doctor.profile.card.description")}</h1>
                <p className={aboutText}>
                    {profile?.description || t("doctor.profile.no_description_available", "No description available.")}
                </p>
            </div>
        </Card>
    );
}

function AboutMeCard({ bio }: { bio: string }) {
    const { t } = useTranslation();
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Stethoscope className={titleIcon}></Stethoscope>
                <h1 className={cardTitleText}>{t("doctor.profile.card.about")}</h1>
            </div>
            <div className={aboutContent}>
                <p className={aboutText}>{bio || t("doctor.profile.no_bio_available", "No biography available.")}</p>
            </div>
        </Card>
    );
}

const cardContent = "flex flex-col items-center gap-3 px-6 py-6 sm:flex-row sm:flex-wrap sm:justify-center";

function CoverageCard({ coverages }: { coverages: CoverageDTO[] }) {
    const { t } = useTranslation();
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <ShieldPlus className={titleIcon}></ShieldPlus>
                <h1 className={cardTitleText}>{t("doctor.profile.coverages")}</h1>
            </div>
            <div className={cardContent}>
                {coverages.length > 0 ? (
                    coverages.map((cov) => (
                        <CoverageComponent
                            key={cov.name}
                            coverageName={cov.name}
                        />
                    ))
                ) : (
                    <EmptySection icon={ShieldPlus} text={t("doctor.profile.no_coverages")} />
                )}
            </div>
        </Card>
    )
}

const componentContainer = "flex justify-center w-full max-w-44 min-w-44 max-h-36 min-h-36 flex-col px-10 py-4 items-center border border-[var(--gray-300)] rounded-lg";

function CoverageComponent({ coverageName }: {
    coverageName: string;
}) {
    return (
        <div className={componentContainer}>
            <div className="bg-(--primary-color) rounded-full p-2 flex items-center justify-center gap-1 text-white mb-2">
                <HeartPlus className="text-white w-5 h-5" />
            </div>
            <h3>{coverageName}</h3>
        </div>
    );
}

function OfficesCard({ offices }: { offices: OfficeDTO[] }) {
    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Hospital className={titleIcon}></Hospital>
                <h1 className={cardTitleText}>{t("doctor.profile.offices")}</h1>
            </div>
            <div className={cardContent}>
                {offices.length > 0 ? (
                    offices.map((office, idx) => (
                        <OfficeComponent
                            key={office.self || idx}
                            officeTitle={office.name || t("doctor.profile.office", "Consultorio")}
                            neighborhoodUrl={office.neighborhood}
                        />
                    ))
                ) : (
                    <EmptySection icon={Hospital} text={t("doctor.profile.no_offices", "No registered offices.")} />
                )}
            </div>
        </Card>
    );
}

const officeContainer = "flex overflow-hidden w-full max-w-44 min-w-44 max-h-36 min-h-36 flex-col px-6 py-6 items-center border border-(--gray-300) rounded-lg";
const iconContainer = "bg-[var(--primary-color)] rounded-full p-2 flex items-center justify-center gap-1 text-white mb-2";
const locationIcon = "h-6 w-6 text-white";
const locationContainer = "flex items-center gap-1 text-[var(--text-light)] text-sm";
const locationTitle = "text-center overflow-clip w-full text-ellipsis text-nowrap font-medium";
const mapPinIcon = "h-4 w-4";

function OfficeComponent({ officeTitle, neighborhoodUrl }: {
    officeTitle: string;
    neighborhoodUrl: string;
}) {
    const { data: neighborhood, isLoading } = useNeighborhood(neighborhoodUrl);
    return (
        <div className={officeContainer}>
            <div className={iconContainer}>
                <Hospital className={locationIcon} />
            </div>
            <h3 className={locationTitle}>{officeTitle}</h3>

            <div className={locationContainer}>
                <div className="text-xs font-semibold text-(--primary-color) mt-1">
                    {isLoading ? (
                        <span className="animate-pulse">...</span>
                    ) : (
                        <div className="flex items-center gap-1">
                            <MapPin className={mapPinIcon} />
                            <span className="text-nowrap">{neighborhood?.name || ""}</span>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

const experienceContent = "pr-6 py-6";
const timelineContainer = "relative pl-10 before:content-[''] before:absolute before:top-3 before:bottom-3 before:left-14 before:w-[2px] before:-translate-x-1/2 before:bg-[var(--gray-300)]";
const editComponentButton = "ml-auto shrink-0 w-26 h-10 bg-transparent text-[var(--primary-dark)]   hover:bg-[rgba(var(--primary-light-rgb),0.2)] hover:border hover:border-[rgba(var(--primary-light-rgb),0.4)] transition-all cursor-pointer";

function ExperienceCard({ experiences, isOwner, updateUrl }: {
    experiences: ExperienceDTO[];
    isOwner: boolean;
    updateUrl: string;
}) {
    const { t } = useTranslation();
    const formatDateRange = (start: string, end?: string) => {
        const startYear = new Date(start).getFullYear();
        const endYear = end ? new Date(end).getFullYear() : t("doctor.profile.present", "Present");
        return `${startYear} – ${endYear}`;
    };
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <BriefcaseBusiness className={titleIcon}></BriefcaseBusiness>
                <h1 className={cardTitleText}>{t("doctor.profile.experiences")}</h1>
                {isOwner && updateUrl && (
                    <EditExperienceDialog
                        experiencesUrl={updateUrl}
                        initialData={experiences}
                        trigger={
                            <Button className={editComponentButton}>
                                <SquarePen className="w-4 h-4 mr-2" />
                                {t("doctor.profile.card.edit")}
                            </Button>
                        }
                    />
                )}
            </div>
            <div className={experienceContent}>
                {experiences.length === 0 ? (
                    <EmptySection icon={BriefcaseBusiness} text={t("doctor.profile.no_experiences")} />
                ) : (
                    <div className={timelineContainer}>
                        {experiences.map((e, idx) => (
                            <ExperienceItem
                                key={`${e.positionTitle}-${idx}`}
                                position={e.positionTitle}
                                organization={e.organizationName}
                                period={formatDateRange(e.startDate, e.endDate)}
                                description={""}
                                isLast={idx === experiences.length - 1}
                            />
                        ))}
                    </div>
                )}
            </div>
        </Card>
    );
}
const input="hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500";

const careerItem = "relative pb-4";
const careerDot = "absolute left-4 top-[6px] -translate-x-1/2 w-6 h-6 rounded-full bg-white border-2 border-[var(--gray-300)] grid place-items-center after:content-[''] after:block after:w-2.5 after:h-2.5 after:rounded-full after:bg-[var(--primary-color)]";
const careerPosition = "text-base font-[600] text-[var(--text-color)] leading-tight";
const careerOrganization = "text-sm font-[500] text-[var(--primary-color)]";
const careerPeriod = "flex items-center gap-1 text-sm text-[var(--text-light)]";
const careerDescription = "text-sm text-[var(--text-color)] leading-6";

function ExperienceItem({ position, organization, period, description, isLast }: {
    position: string;
    organization: string;
    period: string;
    description: string;
    isLast: boolean;
}) {
    return (
        <div className={`${careerItem} ${isLast ? "pb-0" : ""}`}>
            <span className={careerDot} />
            <div className="pl-10 flex flex-col gap-1">
                <h3 className={careerPosition}>{position}</h3>
                <p className={careerOrganization}>{organization}</p>
                <div className={careerPeriod}>
                    <Calendar className="w-4 h-4" />
                    <span>{period}</span>
                </div>
                <p className={careerDescription}>{description}</p>
            </div>
        </div>
    );
}


const certificatesContent = "flex flex-col gap-2 px-6";
const certificatesScrollWrap = "py-6";

function CertificatesCard({ certifications, isOwner, updateUrl }: {
    certifications: CertificationDTO[];
    isOwner: boolean;
    updateUrl: string;
}) {
    const { t } = useTranslation();
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <FileBadge className={titleIcon}></FileBadge>
                <h1 className={cardTitleText}>{t("doctor.profile.certificates")}</h1>
                {isOwner && updateUrl && (
                    <EditCertificatesDialog
                        certsUrl={updateUrl}
                        initialData={certifications}
                        trigger={
                            <Button className={editComponentButton}>
                                <SquarePen className="w-4 h-4 mr-2" />
                                {t("doctor.profile.card.edit")}
                            </Button>
                        }
                    />
                )}
            </div>
            <div className={certificatesScrollWrap}>
                {certifications.length > 0 ? (
                    <div className={certificatesContent}>
                        {certifications.map((cert, idx) => (
                            <CertificateComponent
                                key={idx}
                                certificate={cert.certificateName}
                                issuer={cert.issuingEntity}
                                date={cert.issueDate}
                            />
                        ))}
                    </div>
                ) : (
                    <div className="py-6 flex items-center justify-center">
                        <EmptySection icon={FileBadge} text={t("doctor.profile.no_certificates")} />
                    </div>
                )}
            </div>
        </Card>
    );
}

const certificateContainer = "flex flex-row items-center border border-[var(--gray-300)] rounded-lg py-3";
const badgeContainer = "px-4";
const certificateData = "flex flex-col gap-0";
const badgeIcon = "h-10 w-10 bg-[var(--primary-color)] text-white rounded-full p-2";
const certificateTitle = "text-base font-[500]";
const certificateIssuer = "text-sm text-[var(--text-light)]";
const certificateDate = "flex flex-row items-center gap-1 text-[var(--text-light)]";
const calendarIcon = "w-4 h-4";
const dateText = "text-sm mt-1";

function CertificateComponent({ certificate, issuer, date }: {
    certificate: string;
    issuer: string;
    date: string;
}) {
    return (
        <div className={certificateContainer}>
            <div className={badgeContainer}>
                <BadgeCheck className={badgeIcon} />
            </div>
            <div className={certificateData}>
                <h3 className={certificateTitle}>{certificate}</h3>
                <p className={certificateIssuer}>{issuer}</p>
                <div className={certificateDate}>
                    <Calendar className={calendarIcon} />
                    <p className={dateText}>{date}</p>
                </div>
            </div>
        </div>
    )
}

const ratingsContentWrapper = "w-full max-w-3xl mx-auto pt-6";
const carousel = "relative max-w-3xl w-full mx-auto px-12 -mt-5";
const carouselContentClass = "-ml-4 py-2";
const carouselItemClass = "pl-4 basis-full";

function RatingsCard({ ratings }: { ratings: RatingsDTO[] }) {
    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <UserStar className={titleIcon}></UserStar>
                <h1 className={cardTitleText}>{t("doctor.profile.ratings")}</h1>
            </div>
            <div className={ratingsContentWrapper}>
                {ratings && ratings.length > 0 ? (
                    <Carousel opts={{ align: "start", loop: true }} className={carousel}>
                        <CarouselContent className={carouselContentClass}>
                            {ratings.map((r, idx) => (
                                <CarouselItem key={idx} className={carouselItemClass}>
                                    <div className="py-2">
                                        <RatingCard
                                            className="max-w-xl"
                                            comment={r.comment}
                                            rating={r.rating}
                                            userName={"Anonymous"}
                                        />
                                    </div>
                                </CarouselItem>
                            ))}
                        </CarouselContent>
                        <CarouselPrevious className="left-2 cursor-pointer" />
                        <CarouselNext className="right-2 cursor-pointer" />
                    </Carousel>
                ) : (
                    <div className="pb-6">
                        <EmptySection icon={UserStar} text={t("doctor.profile.no_ratings", "No ratings yet.")} />
                    </div>
                )}
            </div>
        </Card>
    );
}

const editDialogContent = "sm:max-w-[600px] max-h-[85vh] overflow-y-auto p-8 gap-2";
const editDialogInnerContainer = "flex flex-col gap-4 py-4";
const editDialogRowLabel = "grid gap-2";
const editDialogSaveButton = "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] text-white cursor-pointer";
const editDialogCancelButton = "bg-white text-(--primary-color) border border-(--primary-color) hover:bg-(--primary-dark) hover:text-white hover:border-(--primary-dark) cursor-pointer";

function EditProfileDialog({
                               doctorUrl,
                               initialBio,
                               initialDescription,
                               trigger
                           }: {
    doctorUrl: string,
    initialBio: string,
    initialDescription: string,
    trigger: React.ReactNode
}) {
    const { t } = useTranslation();
    const [open, setOpen] = useState(false);
    const [bio, setBio] = useState(initialBio);
    const [desc, setDesc] = useState(initialDescription);
    const mutation = useUpdateDoctorProfileMutation(doctorUrl);

    const handleSave = () => {
        mutation.mutate({ biography: bio, description: desc }, {
            onSuccess: () => {
                setOpen(false);
                toast.success(t("success", "Success"), {
                    description: t("doctor.profile.update_success")
                });
            },
            onError: () => {
                toast.error(t("error", "Error"), {
                    description: t("doctor.profile.update_error")
                });
            }
        });
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>{trigger}</DialogTrigger>
            <DialogContent className={editDialogContent}>
                <DialogHeader>
                    <DialogTitle>{t("doctor.profile.edit_profile")}</DialogTitle>
                    <DialogDescription>{t("doctor.profile.edit_desc")}</DialogDescription>
                </DialogHeader>
                <div className={editDialogInnerContainer}>
                    <div className={editDialogRowLabel}>
                        <Label htmlFor="description">{t("doctor.profile.card.description")}</Label>
                        <Input
                            id="description"
                            className={input}
                            value={desc}
                            onChange={(e) => setDesc(e.target.value)}
                        />
                    </div>
                    <div className={editDialogRowLabel}>
                        <Label htmlFor="bio">{t("doctor.profile.card.about")}</Label>
                        <Textarea
                            id="bio"
                            className="min-h-37.5"
                            value={bio}
                            onChange={(e) => setBio(e.target.value)}
                        />
                    </div>
                </div>
                <DialogFooter>
                    <Button
                        className={editDialogCancelButton}
                        type="button"
                        onClick={() => setOpen(false)}
                        disabled={mutation.isPending}
                    >
                        {t("cancel")}
                    </Button>
                    <Button className={editDialogSaveButton} onClick={handleSave} disabled={mutation.isPending}>
                        {mutation.isPending ? t("saving") : t("save")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}

const editItemDeleteButton = "absolute top-2 right-2 h-8 w-8 text-(--danger) hover:text-white hover:bg-(--danger) cursor-pointer";
const editItemCard = "relative border bg-muted/20 gap-0";
const editItemCardContent = "grid gap-5 p-4 pt-10 sm:pt-4";
const editItemCardRow = "grid sm:grid-cols-2 gap-6";
const editItemCardInput = "space-y-2";
const editDialogAddNew = "w-full border-dashed border-(--gray-400) cursor-pointer";

function EditExperienceDialog({
                                  experiencesUrl,
                                  initialData,
                                  trigger,
                              }: {
    experiencesUrl: string
    initialData: ExperienceDTO[]
    trigger: React.ReactNode
}) {
    const { t } = useTranslation()
    const [open, setOpen] = useState(false)
    const [items, setItems] = useState<ExperienceForm[]>([])
    const mutation = useUpdateDoctorExperienceMutation(experiencesUrl)

    useEffect(() => {
        if (open) {
            setItems(
                initialData.map((e) => ({
                    positionTitle: e.positionTitle,
                    organizationName: e.organizationName,
                    startDate: e.startDate?.toString() ?? "",
                    endDate: e.endDate?.toString() ?? "",
                }))
            )
        }
    }, [open, initialData])

    const updateItem = (index: number, field: keyof ExperienceForm, value: string) => {
        const newItems = [...items]
        newItems[index][field] = value
        setItems(newItems)
    }

    const removeItem = (index: number) => {
        setItems(items.filter((_, i) => i !== index))
    }

    const handleSave = () => {
        const payload = items.map((item) => ({
            ...item,
            endDate: item.endDate === "" ? undefined : item.endDate,
        }))

        mutation.mutate(payload, {
            onSuccess: () => {
                setOpen(false)
                toast.success(t("success"), {
                    description: t("doctor.profile.update_success"),
                })
            },
            onError: () => {
                toast.error(t("error"), {
                    description: t("doctor.profile.update_error"),
                })
            },
        })
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>{trigger}</DialogTrigger>
            <DialogContent className={editDialogContent}>
                <DialogHeader>
                    <DialogTitle>{t("doctor.profile.edit_experience")}</DialogTitle>
                </DialogHeader>

                <div className={editDialogInnerContainer}>
                    {items.map((item, idx) => (
                        <Card key={idx} className={editItemCard}>
                            <Button
                                variant="ghost"
                                size="icon"
                                className={editItemDeleteButton}
                                onClick={() => removeItem(idx)}
                            >
                                <Trash2 className="w-4 h-4" />
                            </Button>

                            <CardContent className={editItemCardContent}>
                                <div className={editItemCardRow}>
                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.position", "Position")}</Label>
                                        <Input
                                            className={input}
                                            value={item.positionTitle}
                                            onChange={(e) => updateItem(idx, "positionTitle", e.target.value)}
                                        />
                                    </div>

                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.organization")}</Label>
                                        <Input
                                            className={input}
                                            value={item.organizationName}
                                            onChange={(e) => updateItem(idx, "organizationName", e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className={editItemCardRow}>
                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.startDate")}</Label>
                                        <DatePicker
                                            value={isoToLocalDate(item.startDate)}
                                            onChange={(d) => updateItem(idx, "startDate", localDateToIso(d))}
                                            fromDate={new Date(1920, 0, 1)}
                                            toDate={new Date()}
                                        />
                                    </div>

                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.endDate")}</Label>
                                        <DatePicker
                                            value={isoToLocalDate(item.endDate || "")}
                                            onChange={(d) => updateItem(idx, "endDate", localDateToIso(d))}
                                            disabled={!item.startDate}
                                            // fromDate={isoToLocalDate(item.startDate)}
                                            fromDate={new Date(1920, 0, 1)}
                                            toDate={new Date()}
                                        />
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    ))}
                    <Button
                        variant="outline"
                        onClick={() =>
                            setItems([
                                ...items,
                                { positionTitle: "", organizationName: "", startDate: "", endDate: "" },
                            ])
                        }
                        className={editDialogAddNew}
                    >
                        <Plus className="w-4 h-4" /> {t("add_new")}
                    </Button>
                </div>

                <DialogFooter>
                    <Button
                        className={editDialogCancelButton}
                        type="button"
                        onClick={() => setOpen(false)}
                        disabled={mutation.isPending}
                    >
                        {t("cancel")}
                    </Button>
                    <Button className={editDialogSaveButton} onClick={handleSave} disabled={mutation.isPending}>
                        {mutation.isPending ? t("saving") : t("save")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    )
}

function EditCertificatesDialog({
                                    certsUrl,
                                    initialData,
                                    trigger
                                }: {
    certsUrl: string,
    initialData: CertificationDTO[],
    trigger: React.ReactNode
}) {
    const { t } = useTranslation();
    const [open, setOpen] = useState(false);
    const [items, setItems] = useState<CertificateForm[]>([]);
    const mutation = useUpdateDoctorCertificatesMutation(certsUrl);

    useEffect(() => {
        if (open) {
            setItems(initialData.map(c => ({
                certificateName: c.certificateName,
                issuingEntity: c.issuingEntity,
                issueDate: c.issueDate.toString()
            })));
        }
    }, [open, initialData]);

    const updateItem = (index: number, field: keyof CertificateForm, value: string) => {
        const newItems = [...items];
        newItems[index][field] = value;
        setItems(newItems);
    };

    const eightyYearsAgo = new Date();
    eightyYearsAgo.setFullYear(eightyYearsAgo.getFullYear() - 80);

    const handleSave = () => {
        mutation.mutate(items, {
            onSuccess: () => {
                setOpen(false);
                toast.success(t("success"), {
                    description: t("doctor.profile.update_success")
                });
            },
            onError: () => {
                toast.error(t("error", "Error"), {
                    description: t("doctor.profile.update_error")
                });
            }
        });
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>{trigger}</DialogTrigger>
            <DialogContent className={editDialogContent}>
                <DialogHeader>
                    <DialogTitle>{t("doctor.profile.edit_certificates")}</DialogTitle>
                </DialogHeader>

                <div className={editDialogInnerContainer}>
                    {items.map((item, idx) => (
                        <Card key={idx} className={editItemCard}>
                            <Button
                                variant="ghost"
                                size="icon"
                                className={editItemDeleteButton}
                                onClick={() => setItems(items.filter((_, i) => i !== idx))}
                            >
                                <Trash2 className="w-4 h-4" />
                            </Button>
                            <CardContent className={editItemCardContent}>
                                <div className={editItemCardInput}>
                                    <Label>{t("doctor.profile.certificateName")}</Label>
                                    <Input value={item.certificateName} onChange={(e) => updateItem(idx, "certificateName", e.target.value)} />
                                </div>
                                <div className={editItemCardRow}>
                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.issuingEntity")}</Label>
                                        <Input value={item.issuingEntity} onChange={(e) => updateItem(idx, "issuingEntity", e.target.value)} />
                                    </div>
                                    <div className={editItemCardInput}>
                                        <Label>{t("doctor.profile.issueDate")}</Label>
                                        <DatePicker
                                            value={isoToLocalDate(item.issueDate)}
                                            onChange={(d) => updateItem(idx, "issueDate", localDateToIso(d))}
                                            fromDate={eightyYearsAgo}
                                            toDate={new Date()}
                                        />
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    ))}
                    <Button variant="outline" onClick={() => setItems([...items, { certificateName: "", issuingEntity: "", issueDate: "" }])} className={editDialogAddNew}>
                        <Plus className="w-4 h-4" /> {t("add_new")}
                    </Button>
                </div>

                <DialogFooter>
                    <Button
                        className={editDialogCancelButton}
                        type="button"
                        onClick={() => setOpen(false)}
                        disabled={mutation.isPending}
                    >
                        {t("cancel")}
                    </Button>
                    <Button className={editDialogSaveButton} onClick={handleSave} disabled={mutation.isPending}>
                        {mutation.isPending ? t("saving") : t("save")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}

export default PublicProfile;