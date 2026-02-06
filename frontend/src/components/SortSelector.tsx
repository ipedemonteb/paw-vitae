import {
    SelectTrigger,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectValue,
    Select
} from "@/components/ui/select.tsx";
import {useTranslation} from "react-i18next";
import type {DoctorQueryParams, PaginationParams} from "@/hooks/useQueryParams.ts";
import {cn} from "@/lib/utils.ts";

interface SortSelectorProps {
    searchParams: PaginationParams & {
        setParams: (updater: (p: URLSearchParams) => void) => void
    } & DoctorQueryParams;
    className?: string;
}

export function SortSelector({ searchParams, className }: { searchParams: SortSelectorProps['searchParams'], className?: string }) {
    const { t } = useTranslation();

    const currentSort = `${searchParams.orderBy || 'name'}-${searchParams.direction || 'asc'}`;

    const handleValueChange = (value: string) => {
        const [orderBy, direction] = value.split('-');

        searchParams.setParams((p) => {
            p.set("orderBy", orderBy);
            p.set("direction", direction);
            p.set("page", "1");
        });
    };

    return (
        <Select value={currentSort} onValueChange={handleValueChange}>
            <SelectTrigger className={cn("w-full justify-between bg-white", className)}>
                <SelectValue placeholder={t("search.sort.title")} />
            </SelectTrigger>
            <SelectContent>
                <SelectGroup>
                    <SelectItem value="name-asc">{t("search.sort.name_asc", "Nombre (A-Z)")}</SelectItem>
                    <SelectItem value="name-desc">{t("search.sort.name_desc", "Nombre (Z-A)")}</SelectItem>

                    <SelectItem value="last_name-asc">{t("search.sort.lastname_asc", "Apellido (A-Z)")}</SelectItem>
                    <SelectItem value="last_name-desc">{t("search.sort.lastname_desc", "Apellido (Z-A)")}</SelectItem>

                    <SelectItem value="rating-desc">{t("search.sort.rating_desc", "Mejor Calificados")}</SelectItem>
                    <SelectItem value="rating-asc">{t("search.sort.rating_asc", "Menor Calificados")}</SelectItem>
                </SelectGroup>
            </SelectContent>
        </Select>
    );
}