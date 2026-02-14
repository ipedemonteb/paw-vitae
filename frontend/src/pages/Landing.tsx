import { Input } from "@/components/ui/input.tsx";
import { SpecialtyCombobox } from "@/components/SpecialtyCombobox.tsx";
import { Button } from "@/components/ui/button.tsx";
import {
    Search,
    PersonStandingIcon,
    Lightbulb,
    CalendarDays,
    ShieldPlus,
    ShieldCheck,
    Pointer
} from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingCard } from "@/components/Rating.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";
import { Link, useNavigate } from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import {useTranslation} from "react-i18next";
import {useDoctors, useDoctorsCount} from "@/hooks/useDoctors.ts";
import {usePatientsCount} from "@/hooks/usePatients.ts";
import {useAllRatings} from "@/hooks/useRatings.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import SearchResultsCard from "@/components/SearchResultCard.tsx";
import {useDebounce} from "use-debounce";
import {Spinner} from "@/components/ui/spinner.tsx";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

const heroSection=
    "mt-25 bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const grayBackground =
    "bg-[var(--background-light)]";

function Landing() {
    return (
        <div>
            <div className={heroSection}>
                <HeroSection />
            </div>
            <MissionSection />
            <div className={grayBackground}>
                <ProcessSection />
            </div>
            <FeaturesSection />
            <div className={grayBackground}>
                <RatingsSection />
            </div>
        </div>
    )
}

const heroContainer =
    "relative overflow-visible block pt-16 pb-20 px-5 mx-auto max-w-6xl w-full";
const heroContent =
    "flex-[1.5] max-w-2xl box-border";
const heroTitle =
    "text-5xl font-bold leading-[1.2] mb-5 text-[var(--text-color)]";
const heroSubtitle =
    "text-lg font-medium text-[var(--text-light)] mb-8";
const heroSearch =
    "mb-10 w-full bg-transparent overflow-visible";
const searchBar =
    "flex items-stretch overflow-visible rounded-md border border-gray-200 bg-white shadow-md relative";
const inputContainer =
    "relative flex-1";
const heroInput =
    "flex-1 h-12 border-0 rounded-none px-4 text-[var(--text-light)] w-full max-w-none md:max-w-85 shadow-none focus-visible:ring-0 hover:bg-[var(--gray-100)]";
const heroCombo =
    "h-12 border-0 min-w-58 rounded-none px-4 text-gray-500 font-normal hover:text-[var(--text-light)] hover:bg-[var(--gray-100)] cursor-pointer shadow-none border-l border-gray-200";
const heroButton =
    "h-12 rounded-l-none rounded-r-sm  px-8 bg-[var(--primary-color)] text-[var(--white)] hover:bg-[var(--primary-dark)] cursor-pointer";
const heroSpace =
    "flex-1 flex justify-end relative";

function HeroSection() {
    const { t } = useTranslation();

    const {claims, isAuthenticated} = useAuth()
    const role = claims?.role?.toUpperCase();
    const isDoctor = role === "ROLE_DOCTOR";


    const { data: doctors = 0, isLoading: loadingDoctors, isError: errorDoctors } = useDoctorsCount();
    const { data: patients = 0, isLoading: loadingPatients, isError: errorPatients } = usePatientsCount();

    const isError = errorDoctors || errorPatients;

    const navigate = useNavigate();
    const [keyword, setKeyword] = useState("");
    const [focus, setFocus] = useState(false)
    const [specialtySelf, setSpecialtySelf] = useState<string | null>(null);
    const [specialtyId, setSpecialtyId] = useState<number | null>(null);

    const doSearch = () => {
        const params = new URLSearchParams();
        const k = keyword.trim();
        if (k) params.set("keyword", k);
        if (specialtyId != null) params.set("specialty", String(specialtyId));
        const qs = params.toString();
        navigate(qs ? `/search?${qs}` : "/search");
    };

    const [open, setOpen] = useState(false)
    const [debounced] = useDebounce(keyword, 500)
    const {data: searchResults, isLoading} = useDoctors({
        keyword: debounced,
        pageSize: 5
    })

    const divRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        setOpen(keyword.length > 0 && !isLoading && focus);
    }, [keyword, focus, isLoading]);


    return (
        <div className={heroContainer}>
            <div className={heroContent}>
                <h1 className={heroTitle}>
                    {t("landing.hero.title")}
                </h1>
                <p className={heroSubtitle}>
                    {t("landing.hero.subtitle")}
                </p>
                {(!isAuthenticated || !isDoctor) && (
                    <div className="flex items-center">
                        <div className={heroSearch}>
                            <div className={searchBar}>
                                <div className={inputContainer}>
                                    <Input
                                        placeholder={t("landing.hero.search_placeholder")}
                                        className={heroInput}
                                        onFocus={() => setFocus(true)}
                                        onBlur={(e) => {
                                            const related = e.relatedTarget;
                                            if (divRef.current && related && divRef.current.contains(related)) return;
                                            setFocus(false);
                                        }}
                                        type="search"
                                        value={keyword}
                                        onChange={(e) => setKeyword(e.target.value)}
                                        onKeyDown={(e) => {
                                            if (e.key === "Enter") doSearch();
                                        }}
                                    />
                                </div>

                                <SpecialtyCombobox
                                    className={`hidden sm:inline-flex ${heroCombo}`}
                                    value={specialtySelf}
                                    onValueChange={(self, id) => {
                                        setSpecialtySelf(self);
                                        setSpecialtyId(id);
                                    }}
                                />

                                <Button className={heroButton} onClick={doSearch}>
                                    <Search className="h-5 w-5" />
                                    {t("landing.hero.search")}
                                </Button>

                                <div
                                    ref={divRef}
                                    onMouseDown={(e) => e.preventDefault()}
                                    className={`${
                                        open
                                            ? "opacity-100 translate-y-0 scale-100 pointer-events-auto"
                                            : "opacity-0 -translate-y-2 scale-95 pointer-events-none"
                                    } absolute top-full z-50 mt-1 max-h-56 overscroll-contain overflow-y-auto border rounded-md bg-white overflow-hidden transition-all duration-200 ease-out origin-top transform
                                      left-0 w-full md:left-0 md:right-0 md:w-auto`}
                                >
                                    {!isLoading && searchResults && searchResults.data.length > 0 && (
                                        searchResults.data.map((d) => (
                                            <SearchResultsCard doctor={d} key={d.self} />
                                        ))
                                    )}

                                    {!isLoading && searchResults && searchResults.data.length === 0 && (
                                        <div className="h-20 w-full flex items-center justify-center gap-1">
                                            <span className="text-(--text-light) text-sm">
                                              {t("search.searchbar.empty")}
                                            </span>
                                        </div>
                                    )}
                                </div>
                            </div>

                        </div>
                    </div>
                )}

                {!isError && (
                    <div className="flex gap-10">
                        <div className="flex flex-col">
                        <span className="text-4xl font-bold text-(--primary-color) flex items-center justify-center h-12">
                          {loadingDoctors ?
                              <div className="flex justify-center items-center">
                                  <Spinner className="size-8"/>
                              </div>
                              :
                              doctors}
                        </span>
                            <span className="text-lg text-(--text-light)">{t("landing.hero.doctors")}</span>
                        </div>
                        <div className="flex flex-col">
                        <span className="text-4xl font-bold text-(--primary-color) flex items-center justify-center h-12">
                          {loadingPatients ?
                              <div className="flex justify-center items-center">
                                  <Spinner className="size-8"/>
                              </div>
                              :
                              patients}
                        </span>
                            <span className="text-lg text-(--text-light)">{t("landing.hero.patients")}</span>
                        </div>
                    </div>
                )}
            </div>
            <div className={heroSpace} />
        </div>
    )
}

const container =
    "mx-auto max-w-6xl w-full px-5 py-16 flex flex-col items-center text-center";
const sectionHeader =
    "max-w-3xl text-center mx-auto mt-0 mb-16";
const sectionTag =
    "inline-block py-1.5 px-4 bg-[var(--landing-light)] text-[var(--primary-color)] rounded-2xl text-sm font-medium mb-4";
const sectionTitle =
    "mb-4 rounded-full px-4 py-1.5 text-4xl font-bold text-[var(--text-color)]";
const sectionText =
    "text-lg text-[var(--text-light)]";
const sectionCards =
    "max-w-5xl w-full flex flex-col md:flex-row gap-8 md:gap-12 items-stretch";
const sectionCard =
    "flex-1 px-8 py-10 gap-0 flex flex-col items-center text-center";
const sectionIcons =
    "w-16 h-16 mb-6 flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-full text-3xl";
const sectionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-full";
const sectionCardTitle =
    "text-xl font-semibold mb-4 text-[var(--text-color)]";
const sectionCardText =
    "text-[var(--text-light)]";

function MissionSection() {
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    {t("landing.mission.tag")}
                </span>
                <h1 className={sectionTitle}>
                    {t("landing.mission.title")}
                </h1>
                <p className={sectionText}>
                    {t("landing.mission.subtitle")}
                </p>
            </div>
            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <PersonStandingIcon className={sectionPersonIcon}/>
                    </div>
                    <h3 className={sectionCardTitle}>{t("landing.mission.accessible_card.title")}</h3>
                    <p className={sectionCardText}>
                        {t("landing.mission.accessible_card.subtitle")}
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={sectionCardTitle}>{t("landing.mission.innovative_card.title")}</h3>
                    <p className={sectionCardText}>
                        {t("landing.mission.innovative_card.subtitle")}
                    </p>
                </Card>
            </div>
        </div>
    )
}

const stepsSection =
    "flex flex-col gap-10 max-w-3xl mx-auto";
const step =
    "flex items-center gap-8 relative";
const stepNumber =
    "shrink-0 flex-none size-16 aspect-square grid place-items-center " +
    "bg-[var(--primary-color)] rounded-full text-[var(--white)] font-bold text-2xl";
const stepContent =
    "p-8 gap-5 flex flex-row items-center";
const stepDescription =
    "flex flex-col items-start text-left";
const stepIcon =
    "shrink-0 flex-none size-16 aspect-square grid place-items-center " +
    "rounded-xl text-[var(--primary-color)] bg-[var(--landing-light)]";
const stepTitle =
    "text-xl font-semibold text-[var(--text-color)]";
const stepText =
    "text-[var(--text-light)]";
const startButton =
    "mt-16 p-6.5 text-base rounded-xl font-medium bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

function ProcessSection() {
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    {t("landing.process.tag")}
                </span>
                <h1 className={sectionTitle}>
                    {t("landing.process.title")}
                </h1>
                <p className={sectionText}>
                    {t("landing.process.subtitle")}
                </p>
            </div>
            <div className={stepsSection}>
                <div className={step}>
                    <div className={stepNumber}>1</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <Search />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>{t("landing.process.first_step.title")}</h3>
                            <p className={stepText}>{t("landing.process.first_step.subtitle")}</p>
                        </div>
                    </Card>
                </div>
                <div className={step}>
                    <div className={stepNumber}>2</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <CalendarDays />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>{t("landing.process.second_step.title")}</h3>
                            <p className={stepText}>{t("landing.process.second_step.subtitle")}</p>
                        </div>
                    </Card>
                </div>
                <div className={step}>
                    <div className={stepNumber}>3</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <ShieldPlus />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>{t("landing.process.third_step.title")}</h3>
                            <p className={stepText}>{t("landing.process.third_step.subtitle")}</p>
                        </div>
                    </Card>
                </div>
            </div>
            <Button className={startButton}>
                <Link to="/search">
                    {t("landing.process.start")}
                </Link>
            </Button>
        </div>
    )
}

function FeaturesSection() {
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    {t("landing.benefits.tag")}
                </span>
                <h1 className={sectionTitle}>
                    {t("landing.benefits.title")}
                </h1>
                <p className={sectionText}>
                    {t("landing.benefits.subtitle")}
                </p>
            </div>
            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <ShieldCheck />
                    </div>
                    <h3 className={sectionCardTitle}>{t("landing.benefits.secure_card.title")}</h3>
                    <p className={sectionCardText}>
                        {t("landing.benefits.secure_card.subtitle")}
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Pointer />
                    </div>
                    <h3 className={sectionCardTitle}>{t("landing.benefits.easy_card.title")}</h3>
                    <p className={sectionCardText}>
                        {t("landing.benefits.easy_card.subtitle")}
                    </p>
                </Card>
            </div>
        </div>
    )
}


const ratingsContainer =
    "w-full max-w-[900px] mx-auto";
const carousel =
    "relative max-w-3xl w-full mx-auto px-12 -mt-5";
const carouselContent =
    "-ml-4 py-2";
const carouselItem =
    "pl-4 basis-full";
const loadingContainer =
    "flex flex-col gap-4 justify-center items-center";
const spinner =
    "h-8 w-8 text-(--gray-400)";
const loadingText =
    "text-md text-(--gray-500)";

function RatingsSection() {
    const { t } = useTranslation();

    const { data: ratings = [], isLoading, isError, refetch, isFetching } = useAllRatings();

    return (
        ratings.length === 0 ? null : (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    {t("landing.ratings.tag")}
                </span>
                <h1 className={sectionTitle}>
                    {t("landing.ratings.title")}
                </h1>
                <p className={sectionText}>
                    {t("landing.ratings.subtitle")}
                </p>
            </div>

            <div className={ratingsContainer}>
                {isLoading ? (
                    <div className={loadingContainer}>
                        <Spinner className={spinner} />
                        <p className={loadingText}>{t("loading")}</p>
                    </div>
                ) : isError ? (
                    <RefetchComponent
                        isFetching={isFetching}
                        onRefetch={() => refetch()}
                        errorText={t("landing.ratings.error")}
                        className="-mt-8"
                        buttonClassName="mt-2"
                    />
                ) :  (
                    <Carousel opts={{ align: "start", loop: true }} className={carousel}>
                        <CarouselContent className={carouselContent}>
                            {ratings.map((r, idx) => (
                                <CarouselItem key={r.self || idx} className={carouselItem}>
                                    <div className="py-2">
                                        <RatingCard
                                            className="max-w-none"
                                            comment={r.comment}
                                            rating={r.rating}
                                            userName={t("landing.ratings.anonymous")}
                                            timeAgo={""}
                                        />
                                    </div>
                                </CarouselItem>
                            ))}
                        </CarouselContent>
                        <CarouselPrevious className="left-2 cursor-pointer" />
                        <CarouselNext className="right-2 cursor-pointer" />
                    </Carousel>
                )}
            </div>
        </div>
    ));
}

export default Landing;
