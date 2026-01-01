import {useAsync} from "@/hooks/useAsync.ts";
import {listCoverages} from "@/data/coverages.ts";
import {useEffect} from "react";


export function useCoverages() {
    const {data, error, loading, run} = useAsync(listCoverages, []);

    useEffect(() => {
        run();
    }, [run]);

    return {
        coverages: data?.data ?? [],
        loading,
        error,
        refetch: () => run(),
    };
}