import { useSearchParams } from "react-router-dom";
import type { AppointmentCollection, AppointmentFilter } from "@/data/appointments";

export type PaginationParams = {
    page: number;
    pageSize: number;
};

type StringRecord = Record<string, string>;

function usePaginatedQueryParams<T extends StringRecord>(
    defaults: PaginationParams & T
): PaginationParams & T & {
    setParams: (updater: (p: URLSearchParams) => void) => void;
} {
    const [sp, setSp] = useSearchParams();

    const page = Number(sp.get("page") ?? defaults.page);
    const pageSize = Number(sp.get("pageSize") ?? defaults.pageSize);

    const extra = Object.keys(defaults).reduce((acc, key) => {
        if (key === "page" || key === "pageSize") return acc;
        acc[key as keyof T] = (sp.get(key) ?? defaults[key]) as T[keyof T];
        return acc;
    }, {} as T);

    return {
        page,
        pageSize,
        ...extra,
        setParams: (updater) => {
            const next = new URLSearchParams(sp);
            updater(next);
            setSp(next);
        },
    };
}


export function useAppointmentsQueryParams() {
    return usePaginatedQueryParams<{
        userId: string;
        doctorId: string;
        collection: AppointmentCollection;
        filter: AppointmentFilter;
    }>({
        page: 1,
        pageSize: 10,
        userId: "",
        doctorId: "",
        collection: "upcoming",
        filter: "all",
    });
}
