import { useCallback, useState } from "react";

export function useMutation<TVars, TRes>(
    fn: (vars: TVars) => Promise<TRes>
) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<unknown | null>(null);

    const mutate = useCallback(async (vars: TVars) => {
        setLoading(true);
        setError(null);
        try {
            const res = await fn(vars);
            setLoading(false);
            return res;
        } catch (e) {
            setError(e);
            setLoading(false);
            throw e;
        }
    }, [fn]);

    return { mutate, loading, error };
}
