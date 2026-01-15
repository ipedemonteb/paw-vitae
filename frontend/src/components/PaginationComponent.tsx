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
import {APPOINTMENTS_PAGE_SIZE} from "@/lib/constants.ts";

type PaginationComponentProps = {
    pagination?: PaginationInfo,
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void}
}

export default function PaginationComponent({pagination, searchParams}: PaginationComponentProps) {
    let page = searchParams.page
    let pageSize = searchParams.pageSize

    if (Number.isNaN(page)) page = 1
    if (Number.isNaN(pageSize)) pageSize = APPOINTMENTS_PAGE_SIZE

    const totalPages = Math.ceil((pagination?.total || 0) / pageSize);
    const setPage = (page: number) => searchParams.setParams(p => p.set("page", String(page)))


    useEffect(() => {
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, [searchParams.page]);

    return (
        <Pagination>
            <PaginationContent className="text-(--text-color) gap-2">
                <PaginationItem className={(pagination && pagination.prev) ? "" : "hidden"}>
                    <PaginationPrevious onClick={() => setPage(page - 1)} />
                </PaginationItem>
                <PaginationItem className={page > 3 ? "" : "hidden"}>
                    <PaginationEllipsis />
                </PaginationItem>
                <PaginationItem className={page > 2 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(page - 2)}>{page - 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={page > 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(page - 1)}>{page - 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem>
                    <PaginationLink isActive>{page}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={page < totalPages ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(page + 1)}>{page + 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={page < totalPages - 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => setPage(page + 2)}>{page + 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={(pagination && pagination.next) ? "" : "hidden"}>
                    <PaginationNext onClick={() => setPage(page + 1)} />
                </PaginationItem>
            </PaginationContent>
        </Pagination>
    )
}