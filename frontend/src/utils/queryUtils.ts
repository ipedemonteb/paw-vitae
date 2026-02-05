import {useEffect, useState} from "react";

export function useDelayedBoolean(value: boolean, delayMs = 400) {
    const [delayed, setDelayed] = useState(false);

    useEffect(() => {
        if (!value) {
            const t = setTimeout(() => setDelayed(false), delayMs);
            return () => clearTimeout(t);
        }
        setDelayed(true)
        return;
    }, [value, delayMs]);

    return delayed;
}