export type PaginationInfo = {
    first?: string;
    last?: string;
    self?: string;
    next?: string;
    prev?: string;
    total?: number;
}

export type PaginationData<T> = {
    data: T,
    pagination: PaginationInfo
}

export type Links = Partial<Record<"first" | "last" | "self" | "prev" | "next", string>>;
