import {SearchX} from "lucide-react";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";

export default function SearchEmpty() {
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
            <SearchX className="text-(--text-light) size-10"/>
            <div className="flex flex-col justify-center items-center gap-0.5">
                <p className="text-xl">{t("search.empty.title")}</p>
                <p className="text-sm">{t("search.empty.message")}</p>
            </div>
        </div>
    )
}