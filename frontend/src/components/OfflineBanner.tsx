import {useIsOfflineLike, useOnlineStatus} from "@/hooks/useOnline";

export default function OfflineBanner() {
    const online = useOnlineStatus();
    const isOfflineLike = useIsOfflineLike()

    return (
        <div className={`w-full h-full z-0 relative  text-center text-sm py-2 transition-all bg-(--warning-light) ${(online && !isOfflineLike) ? "-translate-y-10 opacity-0" : "  translate-y-0 opacity-100 border-b border-b-(--warning-medium)"}`}>
            {!online ? "You’re offline. Some actions may not work." : "Network unreachable"}
        </div>
    );
}
