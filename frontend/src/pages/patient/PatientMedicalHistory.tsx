import {useTranslation} from "react-i18next";
import PastAppointmentComponent from "@/components/PastAppointmentComponent.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {useState} from "react";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";

function PatientMedicalHistory() {
    const { t } = useTranslation();
    const [sort, setSort] = useState<"oldest" | "newest">("oldest");
    const isLoading = false;

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("medical-history.title")}>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t("medical-history.sort.title")}
                    </span>
                    <Select value={sort} onValueChange={(v) => setSort(v as "oldest" | "newest")}>
                        <SelectTrigger className="font-light text-normal cursor-pointer">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="oldest">{t("medical-history.sort.old-first")}</SelectItem>
                            <SelectItem value="newest">{t("medical-history.sort.new-first")}</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </DashboardNavHeader>
            {isLoading ? (
                <DashboardNavLoader/>
            ) : (
                <div className="flex flex-col gap-6">
                    <PastAppointmentComponent />
                    <PastAppointmentComponent />
                    <PastAppointmentComponent />
                    <PastAppointmentComponent />
                </div>
            )}
        </DashboardNavContainer>
    );
}

export default PatientMedicalHistory;