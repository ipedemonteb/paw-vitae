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