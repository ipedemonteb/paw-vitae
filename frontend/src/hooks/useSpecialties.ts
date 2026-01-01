import {useAsync} from "@/hooks/useAsync.ts";
import {useEffect} from "react";
import {listSpecialties} from "@/data/specialties.ts";


export function useSpecialties() {
    const {data, error, loading, run} = useAsync(listSpecialties, []);

    useEffect(() => {
        run();
    }, [run]);

    return {
        specialties: data?.data ?? [],
        loading,
        error,
        refetch: () => run(),
    };
}