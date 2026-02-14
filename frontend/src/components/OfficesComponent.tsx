import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {useTranslation} from "react-i18next";
import {Building} from "lucide-react";
import {useSearchParams} from "react-router-dom";
import {useDoctorOffices} from "@/hooks/useOffices.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import EditOfficeDialog from "@/components/EditOfficeDialog.tsx";
import AddOfficeDialog from "@/components/AddOfficeDialog.tsx";
import DashboardNavSelect from "@/components/DashboardNavSelect.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {DashboardRefetch} from "@/components/DashboardRefetch.tsx";

const officeStatusEnum = ['all', 'active', 'inactive']

function sanitizeQueryParam(q: string | null) {
    return officeStatusEnum.find((value) => value === q);
}

export default function OfficesComponent() {
    const {t} = useTranslation()
    const [sp, setSp] = useSearchParams();
    const officeStatus = sanitizeQueryParam(sp.get("status")) ?? "all"
    const auth = useAuth()
    const { data: offices, isLoading, isError, refetch, isFetching } = useDoctorOffices(`/doctors/${auth.userId}/offices`, { status: officeStatus })

    const delayedLoading = useDelayedBoolean(isLoading);

    if(isError)
        return (
            <DashboardRefetch
                title={t("offices.header.title")}
                text={t("offices.error")}
                isFetching={isFetching}
                refetch={refetch}
            />
        );


    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("offices.header.title")}>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t("offices.filter.message")}
                    </span>
                    <DashboardNavSelect value={officeStatus} onValueChange={(val) => {
                        setSp((p) => {
                            if (val === "all") p.delete("status")
                            else p.set("status", val)
                            return p;
                        })
                    }} content={officeStatusEnum} display={(s: string) => t("offices.filter." + s)}/>
                </div>
            </DashboardNavHeader>
            {delayedLoading ? (
                <DashboardNavLoader/>
            ) : offices && offices.length > 0 ? (
                <div className="grid w-full px-2 gap-x-6 gap-y-3 justify-start grid-cols-[repeat(auto-fill,minmax(14rem,14rem))]">
                    {offices.sort((o1, o2) => o1.name.localeCompare(o2.name)).map((o, i) => (
                        <EditOfficeDialog key={o.self} office={o} animateInDelay={i}/>
                    ))}
                    {officeStatus === "all" && <AddOfficeDialog />}
                </div>
            ) : (
                <DashboardNavEmptyContent title={t("offices.empty.title")} text={officeStatus === "all" ? t("offices.empty.textAll") : t("offices.empty.text")} Icon={Building} />
            )}
        </DashboardNavContainer>
    )
}