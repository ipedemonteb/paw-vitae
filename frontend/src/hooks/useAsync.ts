import { useCallback, useEffect, useRef, useState } from "react";

type AsyncState<T> = {
    data: T | null;
    error: unknown | null;
    loading: boolean;
};

export function useAsync<T, Args extends any[]>(
    fn: (...args: [...Args, AbortSignal?]) => Promise<T>,
    deps: any[] = []
) {
    const [state, setState] = useState<AsyncState<T>>({
        data: null,
        error: null,
        loading: false,
    });

    const abortRef = useRef<AbortController | null>(null);

    const run = useCallback(
        async (...args: Args) => {
            abortRef.current?.abort();
            const ac = new AbortController();
            abortRef.current = ac;

            setState((s) => ({ ...s, loading: true, error: null }));

            try {
                const data = await fn(...args, ac.signal);
                setState({ data, error: null, loading: false });
                return data;
            } catch (error) {
                if ((error as any)?.name === "CanceledError" || (error as any)?.code === "ERR_CANCELED") {
                    return;
                }
                setState({ data: null, error, loading: false });
                throw error;
            }
        },
        deps
    );

    useEffect(() => {
        return () => abortRef.current?.abort();
    }, []);

    return { ...state, run };
}
