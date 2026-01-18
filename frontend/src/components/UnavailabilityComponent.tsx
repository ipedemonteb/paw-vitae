import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {useTranslation} from "react-i18next";


export default function UnavailabilityComponent() {
    const {t} = useTranslation()


    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("unavailability.headerTitle")} children={undefined}/>
        </DashboardNavContainer>

        )
}