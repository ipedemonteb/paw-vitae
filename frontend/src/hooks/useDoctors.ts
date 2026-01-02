import { useEffect } from "react";
import {listDoctors, type DoctorsQuery} from "@/data/doctors";
import { useAsync } from "./useAsync";


//EXAMPLE USAGE: const { doctors, loading, error, refetch } = useDoctors({ page, keyword, weekdays });
export function useDoctors(query: DoctorsQuery) {
    const { data, error, loading, run } = useAsync(listDoctors, []);

    useEffect(() => {
        run(query);
    }, [run, JSON.stringify(query)]);

    return {
        doctors: data?.data ?? [],
        total: data?.total ?? 0,
        links: data?.links,
        loading,
        error,
        refetch: () => run(query),
    };
}


