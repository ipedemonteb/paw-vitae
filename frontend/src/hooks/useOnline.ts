import {isAxiosError} from "axios";
import {useQuery} from "@tanstack/react-query";
import {api} from "@/data/Api.ts";
import {useEffect, useState} from "react";

function isNetworkErrorLike(err: unknown) {
    if  (!isAxiosError(err)) return false;

    if (!err.response) return true;

    return err.code === "ECONNABORTED" || err.code === "ERR_NETWORK";
}

function useConnectivity(enabled: boolean) {
    return useQuery({
        queryKey: ['connectivity'],
        queryFn: async () => {
            await api.get("/coverages");
            return true;
        },
        refetchInterval: 10_000,
        retry: false,
        select: () => true,
        meta: { kind: "connectivity"},
        enabled: enabled
    })
}

export function useIsOfflineLike() {
    const browserOnline = navigator.onLine
    const q = useConnectivity(browserOnline)
    if (q.isError && isNetworkErrorLike(q.error)) return true;
    return !browserOnline;

}

export function useOnlineStatus() {
    const [online, setOnline] = useState(() => navigator.onLine);

    useEffect(() => {
        const onOnline = () => setOnline(true);
        const onOffline = () => setOnline(false);
        window.addEventListener("online", onOnline);
        window.addEventListener("offline", onOffline);
        return () => {
            window.removeEventListener("online", onOnline);
            window.removeEventListener("offline", onOffline);
        };
    }, []);

    return online;
}