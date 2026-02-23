import { Card } from "@/components/ui/card.tsx";
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
import {Info, ChevronsUpDown, ArrowLeft} from "lucide-react";
import { useTranslation } from "react-i18next";
import PastAppointmentComponent from "@/components/PastAppointmentComponent.tsx";
import { useAppointmentsQueryParams } from "@/hooks/useQueryParams.ts";
import { useAppointments } from "@/hooks/useAppointments.ts";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.tsx";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useNavigate, useParams} from "react-router-dom";
import { Button } from "@/components/ui/button.tsx";
import {usePatientById} from "@/hooks/usePatients.ts";
import {LoadingFullPageComponent} from "@/components/LoadingFullPageComponent.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

const historyBackground = "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer = "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const historyContainer = "p-0 pb-8";
const historyHeader = "flex flex-col items-center py-8 rounded-t-lg gap-3 px-5 bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const historyTitle = "font-bold text-4xl text-center text-[var(--text-color)]";
const historySubtitle = "text-center text-[var(--text-light)] max-w-3xl";
const historyContent = "flex flex-col px-8 gap-4";
const historyAppointmentsCard = "px-6";
const historyUpperContainer = "flex flex-col sm:flex-row justify-between items-center";
const pastAppointments = "text-lg mb-2 sm:mb-0 text-[var(--text-color)] font-bold";
const historySort = "flex flex-row items-center gap-1 text-[var(--text-light)]";
const historySortIcon = "w-5 h-5 p-0 m-0";
const sortText = "text-md mr-2";
const pastAppointmentsContainer = "flex flex-col gap-4";
const selectTrigger = "font-light text-normal text-(--text-color) cursor-pointer select-none focus:outline-none focus:ring-0 focus-visible:ring-0 focus-visible:ring-offset-0";
const noAppointments = "flex flex-row items-center justify-center px-4 py-14 text-[var(--gray-500)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";
const goBackButton = "w-3xs bg-white text-[var(--primary-color)] border border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border hover:border-[var(--primary-dark)] hover:text-white cursor-pointer";


function MedicalHistory() {
    const { t } = useTranslation();
    const { patientId } = useParams<{ patientId: string }>();
    const searchParams = useAppointmentsQueryParams();
    const sort: "asc" | "desc" = searchParams.sort === "desc" ? "desc" : "asc";
    const navigate = useNavigate();

    const handleBackToAppointments = () => {
        navigate(-1);
    };

    const { data: appointments, isLoading, isError, refetch, isFetching } = useAppointments({
        collection: "history",
        filter: "completed",
        sort: sort,
        page: searchParams.page,
        pageSize: searchParams.pageSize,
        patientId: patientId
    });

    const { data: patient, isLoading: isLoadingPatient } = usePatientById(patientId);

    const completed = (appointments?.data ?? []).filter(a => a.status === "completo");

    const loading = isLoading || isLoadingPatient;

    if (useDelayedBoolean(loading)) return (
        <LoadingFullPageComponent/>
    )

    const renderListContent = () => {

        if (isLoading) {
            return <AppointmentsSectionLoader />;
        }

        if (isError) {
            return (
                <div className={noAppointments}>
                    <RefetchComponent
                        isFetching={isFetching}
                        onRefetch={refetch}
                        errorText={t("medical-history.error")}
                    />
                </div>
            );
        }

        if (completed.length === 0) {
            return <NoPastAppointmentComponent />;
        }

        return (
            <>
                {completed.map((a) => (
                    <PastAppointmentComponent key={a.self} appointment={a} />
                ))}
                {appointments?.pagination && (
                    <PaginationComponent pagination={appointments.pagination} searchParams={searchParams} />
                )}
            </>
        );
    };

    return (
        <div className={historyBackground}>
            <div className={cardContainer}>
                <Card className={historyContainer}>
                    <div className={historyHeader}>
                        <h1 className={historyTitle}>{t("medical-history.title")}</h1>
                        <p className={historySubtitle}>{t("medical-history.subtitle")}</p>
                    </div>

                    <div className={historyContent}>
                        <PatientProfileCard patient={patient} />

                        <Card className={historyAppointmentsCard}>
                            <div className={historyUpperContainer}>
                                <p className={pastAppointments}>{t("medical-history.past")}</p>
                                <div className={historySort}>
                                    <ChevronsUpDown className={historySortIcon} />
                                    <p className={sortText}>{t("medical-history.sort.title")}</p>
                                    <Select
                                        value={sort}
                                        onValueChange={(v) => {
                                            const next = v as "asc" | "desc";
                                            searchParams.setParams((p) => {
                                                p.set("sort", next);
                                                p.set("page", "1");
                                            });
                                        }}
                                        disabled={isLoading}
                                    >
                                        <SelectTrigger className={selectTrigger}>
                                            <SelectValue />
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectItem value="asc">{t("medical-history.sort.old-first")}</SelectItem>
                                            <SelectItem value="desc">{t("medical-history.sort.new-first")}</SelectItem>
                                        </SelectContent>
                                    </Select>
                                </div>
                            </div>
                        </Card>

                        <div className={pastAppointmentsContainer}>
                            {renderListContent()}
                        </div>
                        <div className="flex justify-center w-full mt-6">
                            <Button
                                className={goBackButton}
                                onClick={handleBackToAppointments}
                                type="button"
                            >
                                <ArrowLeft className="h-5 w-5 mr-2" />
                                <p>{t("medical-history.back")}</p>
                            </Button>
                        </div>
                    </div>

                </Card>
            </div>
        </div>
    );
}

function NoPastAppointmentComponent() {
    const { t } = useTranslation();
    return (
        <div className={noAppointments}>
            <Info />
            <p>{t("medical-history.no-appointments")}</p>
        </div>
    );
}

function AppointmentsSectionLoader() {
    return (
        <div className="w-ful flex flex-col gap-2">
            {Array.from({length: 5}).map((_, i) => (
                <Skeleton key={i} className="h-52 w-full"/>
            ))}
        </div>
    );
}

export default MedicalHistory;