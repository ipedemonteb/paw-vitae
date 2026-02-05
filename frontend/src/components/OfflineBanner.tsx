import {useIsOfflineLike, useOnlineStatus} from "@/hooks/useOnline";
import {useState} from "react";
import {TriangleAlert, X} from "lucide-react";

export default function OfflineBanner() {
    const online = useOnlineStatus();
    const isOfflineLike = useIsOfflineLike()
    const [open, setOpen] = useState(true)

    return (
        <div className={`w-full h-full gap-4 z-0 flex items-center justify-center relative duration-300 text-sm py-2 transition-all bg-(--warning-light) ${((online && !isOfflineLike) || !open) ? "-translate-y-10 opacity-0" : "  translate-y-0 opacity-100 border-b border-b-(--warning-medium)"}`}>
            <TriangleAlert className="text-(--warning) size-5"/>
            <p>{!online ? "You’re offline. Some actions may not work." : "Network unreachable"}</p>
            <X className="absolute right-2 cursor-pointer size-5" onClick={() => setOpen(false)} />
        </div>
    );
}
