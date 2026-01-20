import {useTranslation} from "react-i18next";
import PastAppointmentComponent from "@/components/PastAppointmentComponent.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {NativeSelect, NativeSelectOption} from "@/components/ui/native-select.tsx";

function PatientMedicalHistory() {
    const { t } = useTranslation();
    return (
        <DashboardNavContainer>
            <DashboardNavHeader title="Medical History">
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        Sort By:
                    </span>
                    <NativeSelect className="font-normal cursor-pointer">
                        <NativeSelectOption value="oldest">{t("medical-history.sort.old-first")}</NativeSelectOption>
                        <NativeSelectOption value="newest">{t("medical-history.sort.new-first")}</NativeSelectOption>
                    </NativeSelect>
                </div>
            </DashboardNavHeader>
            <PastAppointmentComponent />
            <PastAppointmentComponent />
            <PastAppointmentComponent />
            <PastAppointmentComponent />
        </DashboardNavContainer>
    );
}

export default PatientMedicalHistory;