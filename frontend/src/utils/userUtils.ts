export function initialsFallback(name?: string | null, lastName?: string | null) {
    const a = name?.trim()?.[0] ?? "";
    const b = lastName?.trim()?.[0] ?? "";
    return (a + b).toUpperCase() || "U";
}