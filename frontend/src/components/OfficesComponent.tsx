import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {useTranslation} from "react-i18next";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Check, ChevronDown, Building} from "lucide-react";
import {useState} from "react";
import {useSearchParams} from "react-router-dom";
import {useDoctorOffices} from "@/hooks/useDoctors.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import EditOfficeDialog from "@/components/EditOfficeDialog.tsx";

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
            <DashboardNavHeader title={t("offices.headerTitle")}>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t("offices.filter.message")}:
                    </span>
                    <Popover open={open} onOpenChange={setOpen}>
                        <PopoverTrigger asChild>
                            <Button
                                className="flex bg-white text-black min-w-20 max-w-25 justify-between  hover:bg-gray-100 flex-row font-light items-center text-sm gap-1 border rounded-md cursor-pointer"
                                variant="outline"
                                role="combobox"
                                aria-expanded={open}
                            >
                                {t("offices.filter." + officeStatus)}
                                <ChevronDown size={20}/>
                            </Button>
                        </PopoverTrigger>
                        <PopoverContent className="min-w-fit max-w-28 p-0">
                            {officeStatusEnum.map(o => (
                                <Button key={o} value={o} onClick={() => {
                                    setSp((p) => {
                                        if (o === "all") p.delete("status")
                                        else p.set("status", o)
                                        return p;
                                    })
                                    setOpen(false)
                                }}
                                        className="w-full text-xs justify-between bg-white font-light hover:bg-gray-100 hover:text-black flex text-(--text-light)">
                                    {t("offices.filter." + o)}
                                    <Check className={(officeStatus === o ? "opacity-100 text-(--text-light) size-4" : "opacity-0")} />
                                </Button>
                            ))}
                        </PopoverContent>
                    </Popover>
                </div>
            </DashboardNavHeader>
            {isLoading ? (
                <DashboardNavLoader item={t("offices.loadingText")}/>
            ) : offices && offices.length > 0 ? (
                <div className="flex flex-wrap gap-x-6 gap-y-3 justify-center w-fit px-2">
                    {offices.map(o => (
                        <EditOfficeDialog key={o.self} office={o}/>
                    ))}
                </div>
            ) : (
                <DashboardNavEmptyContent title={t("offices.empty.title")} text={officeStatus === "all" ? t("offices.empty.textAll") : t("offices.empty.text")} Icon={Building} />
            )}
        </DashboardNavContainer>
    )
}