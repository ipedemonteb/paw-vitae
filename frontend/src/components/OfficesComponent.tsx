import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {useTranslation} from "react-i18next";
import { Building, Info} from "lucide-react";
import {useState} from "react";
import {useSearchParams} from "react-router-dom";
import {useDoctorOffices} from "@/hooks/useDoctors.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import EditOfficeDialog from "@/components/EditOfficeDialog.tsx";
import {HoverCard, HoverCardContent, HoverCardTrigger} from "@/components/ui/hover-card.tsx";
import AddOfficeDialog from "@/components/AddOfficeDialog.tsx";
import {SelectTrigger, Select, SelectContent, SelectValue, SelectItem} from "@/components/ui/select.tsx";

const officeStatusEnum = ['all', 'active', 'inactive']

function sanitizeQueryParam(q: string | null) {
    return officeStatusEnum.find((value) => value === q);
}

export default function OfficesComponent() {
    const {t} = useTranslation()
    const [open, setOpen] = useState(false)
    const [sp, setSp] = useSearchParams();
    const officeStatus = sanitizeQueryParam(sp.get("status")) ?? "all"
    const auth = useAuth()
    const {data: offices, isLoading} = useDoctorOffices(`/doctors/${auth.userId}/offices`, { status: officeStatus })
    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={
                <div className="flex gap-2 items-center justify-center">
                    {t("offices.headerTitle")}
                    <HoverCard openDelay={10} closeDelay={40}>
                        <HoverCardTrigger asChild>
                            <Info className="size-4 text-(--text-light)" />
                        </HoverCardTrigger>
                        <HoverCardContent side="top" className="bg-gray-50 text-(--text-light) max-w-40 flex justify-center items-center w-fit text-sm">
                            Hover over an office to start managing it
                        </HoverCardContent>
                    </HoverCard>
                </div>
            }>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t("offices.filter.message")}
                    </span>
                    <Select
                        value={officeStatus}
                        onValueChange={(val) => {
                            setSp((p) => {
                                if (val === "all") p.delete("status")
                                else p.set("status", val)
                                return p;
                            })
                            setOpen(false)
                        }}
                        open={open}
                        onOpenChange={setOpen}
                    >
                        <SelectTrigger className="bg-white text-black hover:bg-gray-100 font-light text-sm border rounded-md cursor-pointer">
                            <SelectValue/>
                        </SelectTrigger>
                        <SelectContent position="popper" className="min-w-fit max-w-28 p-0">
                            {officeStatusEnum.map(o => (
                                <SelectItem key={o} value={o}>
                                    {t("offices.filter." + o)}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                    <AddOfficeDialog/>
                </div>
            </DashboardNavHeader>
            {isLoading ? (
                <DashboardNavLoader/>
            ) : offices && offices.length > 0 ? (
                <div className="flex relative flex-wrap gap-x-6 gap-y-3 justify-center w-fit px-2 pt-6">
                    {offices.map((o, i) => (
                        <EditOfficeDialog key={o.self} office={o} animateInDelay={i}/>
                    ))}
                </div>
            ) : (
                <DashboardNavEmptyContent title={t("offices.empty.title")} text={officeStatus === "all" ? t("offices.empty.textAll") : t("offices.empty.text")} Icon={Building} />
            )}
        </DashboardNavContainer>
    )
}