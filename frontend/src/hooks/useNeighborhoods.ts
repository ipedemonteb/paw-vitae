import {useAsync} from "@/hooks/useAsync.ts";
import {listNeighborhoods} from "@/data/neighborhoods";
import {useEffect} from "react";


export function useNeighborhoods() {
    const {data, error, loading, run} = useAsync(listNeighborhoods, []);

    useEffect(() => {
        run();
    }, [run]);

    return {
        neighborhoods: data?.data ?? [],
        loading,
        error,
        refetch: () => run(),
    };
}