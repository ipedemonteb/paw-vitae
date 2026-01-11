import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem, PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import type {PaginationInfo} from "@/lib/types.ts";
import type {AppointmentsQueryParams} from "@/hooks/useAppointmentsQueryParams.ts";

type PaginationComponentProps = {
    pagination?: PaginationInfo,
    searchParams: AppointmentsQueryParams
}

export default function PaginationComponent({pagination, searchParams}: PaginationComponentProps) {
    const totalPages = Math.ceil((pagination?.total || 0) / searchParams.pageSize);
    return (
        <Pagination>
            <PaginationContent className="text-(--text-color) gap-2">
                <PaginationItem className={(pagination && pagination.prev) ? "" : "hidden"}>
                    <PaginationPrevious onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page - 1)))} />
                </PaginationItem>
                <PaginationItem className={searchParams.page > 3 ? "" : "hidden"}>
                    <PaginationEllipsis />
                </PaginationItem>
                <PaginationItem className={searchParams.page > 2 ? "" : "hidden"}>
                    <PaginationLink onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page - 2)))}>{searchParams.page - 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page > 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page - 1)))}>{searchParams.page - 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem>
                    <PaginationLink isActive>{searchParams.page}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page < totalPages ? "" : "hidden"}>
                    <PaginationLink onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page + 1)))}>{searchParams.page + 1}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={searchParams.page < totalPages - 1 ? "" : "hidden"}>
                    <PaginationLink onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page + 2)))}>{searchParams.page + 2}</PaginationLink>
                </PaginationItem>
                <PaginationItem className={(pagination && pagination.next) ? "" : "hidden"}>
                    <PaginationNext onClick={() => searchParams.setParams(p => p.set("page", String(searchParams.page + 1)))} />
                </PaginationItem>
            </PaginationContent>
        </Pagination>
    )
}