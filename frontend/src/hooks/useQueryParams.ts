import { useSearchParams } from "react-router-dom";
import type { AppointmentCollection, AppointmentFilter } from "@/data/appointments";
import {APPOINTMENTS_PAGE_SIZE, DOCTORS_PAGE_SIZE} from "@/lib/constants.ts";

export type PaginationParams = {
    page: number;
    pageSize: number;
};

type StringRecord = Record<string, string | string[]>;

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
        const def = defaults[key as keyof T];
        if (Array.isArray(def)) {
            const values = sp.getAll(key);
            if (values.length === 0) {
                acc[key as keyof T] = def;
            } else {
                acc[key as keyof T] = values as T[keyof T];
            }
        } else {
            acc[key as keyof T] = (sp.get(key) ?? def) as T[keyof T];
        }
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
        sort: "asc" | "desc";
    }>({
        page: 1,
        pageSize: APPOINTMENTS_PAGE_SIZE,
        userId: "",
        doctorId: "",
        collection: "upcoming",
        filter: "all",
        sort: "asc"
    });
}

export type DoctorQueryParams = {
    specialty: string,
    coverage: string,
    weekdays: string[],
    keyword: string,
    orderBy: string,
    direction: "asc" | "desc"
}

export function useDoctorQueryParams() {
    return usePaginatedQueryParams<DoctorQueryParams>({
        page: 1,
        pageSize: DOCTORS_PAGE_SIZE,
        specialty: "",
        coverage: "",
        weekdays: [],
        keyword: "",
        orderBy: "name",
        direction: "asc"
    })
}
