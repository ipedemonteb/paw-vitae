import {useIsOfflineLike, useOnlineStatus} from "@/hooks/useOnline";
import {useState} from "react";
import {TriangleAlert, X} from "lucide-react";
import { useTranslation } from "react-i18next"
export default function OfflineBanner() {
    const online = useOnlineStatus();
    const isOfflineLike = useIsOfflineLike()
    const [open, setOpen] = useState(true)
    const {t} = useTranslation()
    return (
        <div className={`w-full overflow-hidden h-full gap-4 z-0 flex items-center justify-center relative duration-300 ease-out text-sm transition-all bg-(--warning-light) ${((online && !isOfflineLike) || !open) ? "-translate-y-10 opacity-0 max-h-0 border-b-0 py-0" : "  translate-y-0 opacity-100 border-b border-b-(--warning-medium) py-2"}`}>
            <TriangleAlert className="text-(--warning) size-5"/>
            <p>{!online ? t("offlineBanner.offlineMessage") : t("offlineBanner.networkUnreachable")}</p>
            <X className="absolute right-2 cursor-pointer size-5" onClick={() => setOpen(false)} />
        </div>
    );
}
