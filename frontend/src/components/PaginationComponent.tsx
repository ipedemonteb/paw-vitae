import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem, PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import type {PaginationInfo} from "@/lib/types.ts";
import type {PaginationParams} from "@/hooks/useQueryParams.ts";
import {useEffect} from "react";

type PaginationComponentProps = {
    pagination?: PaginationInfo,
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void}
}

export default function PaginationComponent({pagination, searchParams}: PaginationComponentProps) {
    const totalPages = Math.ceil((pagination?.total || 0) / searchParams.pageSize);
    const setPage = (page: number) => searchParams.setParams(p => p.set("page", String(page)))

    useEffect(() => {
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, [searchParams.page]);

    return (
        <Pagination>
            <PaginationContent className="text-(--text-color) gap-2">
                <PaginationItem className={(pagination && pagination.prev) ? "" : "hidden"}>
                    <PaginationPrevious onClick={() => setPage(searchParams.page - 1)} />
                </PaginationItem>
                <PaginationItem className={searchParams.page > 3 ? "" : "hidden"}>
                    <PaginationEllipsis />
                </PaginationItem>
                <PaginationItem className={searchParams.page > 2 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(searchParams.page - 2)}>{searchParams.page - 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page > 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(searchParams.page - 1)}>{searchParams.page - 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem>
                    <PaginationLink isActive>{searchParams.page}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page < totalPages ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(searchParams.page + 1)}>{searchParams.page + 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page < totalPages - 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(searchParams.page + 2)}>{searchParams.page + 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={(pagination && pagination.next) ? "" : "hidden"}>
                    <PaginationNext onClick={() => setPage(searchParams.page + 1)} />
                </PaginationItem>
            </PaginationContent>
        </Pagination>
    )
}