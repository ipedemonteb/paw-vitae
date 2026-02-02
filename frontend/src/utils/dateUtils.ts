export function timeToMinutes(t: string): number {
    const [hh, mm] = t.split(":").map(Number);
    return hh * 60 + mm;
}

export function formatLongDate(date: string | undefined, locale: string): string {
    if (!date) return "";

    const d = isoToLocalDate(date);
    if (!d || isNaN(d.getTime())) return "";

    return new Intl.DateTimeFormat(locale, {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    }).format(d);
}

export function formatTimeHM(date: string, locale: string): string {
    const d = new Date(date);
    if (isNaN(d.getTime())) return "";

    return new Intl.DateTimeFormat(locale, {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    }).format(d);
}

export function isoToLocalDate(iso?: string): Date | undefined {
    if (!iso) return undefined
    const s = iso.trim()
    if (!s) return undefined
    const [y, m, d] = s.split("-").map(Number)
    if (!y || !m || !d) return undefined
    return new Date(y, m - 1, d)
}

export function localDateToIso(date?: Date): string {
    if (!date) return ""
    const y = date.getFullYear()
    const m = String(date.getMonth() + 1).padStart(2, "0")
    const d = String(date.getDate()).padStart(2, "0")
    return `${y}-${m}-${d}`
}

export const timeOptions = [
    "08:00",
    "09:00",
    "10:00",
    "11:00",
    "12:00",
    "13:00",
    "14:00",
    "15:00",
    "16:00",
    "17:00",
    "18:00",
    "19:00",
    "20:00"
];

export function ensureSeconds(hm: string): string {
    if (!hm) return "";
    return hm.length === 5 ? `${hm}:00` : hm;
}

export function normalizeTime(value: unknown): string {
    if (typeof value !== "string") return "";
    if (value.length >= 5) return value.slice(0, 5);
    return value;
}

export const dayOptions = [
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATURDAY",
    "SUNDAY",
];

export const DB_DAY_ORDER: Array<0 | 1 | 2 | 3 | 4 | 5 | 6> = [1, 2, 3, 4, 5, 6, 0];

export function labelFromDbDay(dbDay: number) {
    return dbDay === 0 ? dayOptions[6] : dayOptions[dbDay - 1];
}

export function normalizeDayIndex(value: unknown): number | null {
    const n =
        typeof value === "number"
            ? value
            : typeof value === "string"
                ? Number(value.trim())
                : NaN;

    if (!Number.isFinite(n)) return null;

    const idx = Math.trunc(n); // 0..6
    if (idx < 0 || idx >= dayOptions.length) return null;

    return idx;
}