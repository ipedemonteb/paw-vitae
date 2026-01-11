// hooks/useAppointmentsQueryParams.ts
import { useSearchParams } from "react-router-dom";

export type AppointmentsQueryParams = {
    page: number;
    pageSize: number;
    userId: string;
    doctorId: string;
    collection: string;
    filter: string;
    setParams: (updater: (p: URLSearchParams) => void) => void;
}

export function useAppointmentsQueryParams(defaults = { page: 1, pageSize: 10 }): AppointmentsQueryParams {
    const [sp, setSp] = useSearchParams();

    const page = Number(sp.get("page") ?? defaults.page);
    const pageSize = Number(sp.get("pageSize") ?? defaults.pageSize);

    const userId = sp.get("userId") ?? "";
    const doctorId = sp.get("doctorId") ?? "";
    const collection = sp.get("collection") ?? "upcoming";
    const filter = sp.get("filter") ?? "all";


    return {
        page,
        pageSize,
        userId,
        doctorId,
        collection,
        filter,
        setParams: (updater: (p: URLSearchParams) => void) => {
            const next = new URLSearchParams(sp);
            updater(next);
            setSp(next);
        }
    };
}
