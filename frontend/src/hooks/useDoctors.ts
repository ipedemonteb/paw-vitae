import {listDoctors, type DoctorsQuery, getDoctor, getDoctorImage} from "@/data/doctors";
import {keepPreviousData, useQuery} from "@tanstack/react-query";
import {useEffect, useState, useMemo} from "react";

export function useDoctors(query: DoctorsQuery) {
    return useQuery({
        queryKey: ['auth', 'doctors', query],
        queryFn: () => listDoctors(query),
        placeholderData: keepPreviousData
    })
}

export function useDoctor(id?: string) {
    return useQuery({
        queryKey: ["doctor", id],
        queryFn: () => getDoctor(id!),
        enabled: !!id,
    });
}

const isNumericId = (id?: string) => !!id && /^\d+$/.test(id);

export function useDoctorImageUrl(id?: string) {
    const enabled = useMemo(() => isNumericId(id), [id]);

    const query = useQuery({
        queryKey: ["auth", "doctor", id, "image"],
        queryFn: () => getDoctorImage(id!),
        enabled,
        staleTime: 5 * 60_000,
    });

    const [url, setUrl] = useState<string | null>(null);

    useEffect(() => {
        if (!query.data) {
            setUrl(null);
            return;
        }
        const objectUrl = URL.createObjectURL(query.data);
        setUrl(objectUrl);
        return () => URL.revokeObjectURL(objectUrl);
    }, [query.data]);

    return { ...query, url };
}