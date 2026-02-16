import {CircleAlert, SearchX} from "lucide-react";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";

type SearchEmptyProps = {
    error: boolean
    isRefetching?: boolean
    refetch?: () => void
}

export default function SearchEmpty({error, isRefetching, refetch}: SearchEmptyProps) {
    const {t} = useTranslation()
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, []);

    const base =
        "w-full h-90 bg-gray-100 rounded-2xl mb-4 flex flex-col justify-center items-center gap-2 will-change-transform";
    const transition = "transition-transform transition-opacity duration-300 ease-out";
    const from = "opacity-0 translate-y-2";
    const to = "opacity-100 translate-y-0";

    return (
        <div className={`${base} ${transition} ${mounted ? to : from}`}>
            {error ? (
                <CircleAlert className="text-(--text-light) size-10"/>
                ) : (
                <SearchX className="text-(--text-light) size-10"/>
            )}
            <div className="flex flex-col justify-center items-center gap-0.5">
                <p className="text-xl">{error ? t("search.error.title") : t("search.empty.title")}</p>
                <p className="text-sm">{error? t("search.error.message")  : t("search.empty.message")}</p>
                {error && (
                    isRefetching ? <Spinner className="size-6 text-(--text-light) my-2.5"/> : <Button disabled={isRefetching} className="mt-2 cursor-pointer text-white font-semibold bg-(--gray-400) hover:bg-(--gray-500)" onClick={() => refetch && refetch()}>Retry</Button>
                )}
            </div>
        </div>
    )
}