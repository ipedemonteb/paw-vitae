export function timeToMinutes(t: string): number {
    const [hh, mm] = t.split(":").map(Number);
    return hh * 60 + mm;
}

export function minutesToTime(mm: number): string {
    const hh = Math.floor(mm / 60);
    const m = mm % 60;
    return `${String(hh).padStart(2, "0")}:${String(m).padStart(2, "0")}`;
}

export function buildTimeSlotsForDay(
    availabilities: { startTime: string; endTime: string }[],
    slotMinutes = 60
): string[] {
    const slots: string[] = [];

    availabilities.forEach((a) => {
        let start = timeToMinutes(a.startTime);
        const end = timeToMinutes(a.endTime);

        while (start + slotMinutes <= end) {
            slots.push(minutesToTime(start));
            start += slotMinutes;
        }
    });

    return Array.from(new Set(slots)).sort();
}

export function dateKey(d: Date): number {
    return d.getFullYear() * 10000 + (d.getMonth() + 1) * 100 + d.getDate();
}

export function isoDateKey(iso: string): number {
    const [y, m, d] = iso.split("-").map(Number);
    return y * 10000 + m * 100 + d;
}

export function startOfDay(d: Date) {
    const x = new Date(d);
    x.setHours(0, 0, 0, 0);
    return x;
}

export function formatLongDate(date: string | undefined, locale: string): string {
    if (!date) return "";

    const d = new Date(date);
    if (isNaN(d.getTime())) return "";

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
    "20:00",
];

export function normalizeTime(value: unknown): string {
    if (typeof value !== "string") return "";
    if (value.length >= 5) return value.slice(0, 5);
    return value;
}

export const dayOptions = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
];

export function normalizeDay(value: unknown): string {
    const n =
        typeof value === "number"
            ? value
            : typeof value === "string"
                ? Number(value.trim())
                : NaN;
    if (!Number.isFinite(n)) return "";
    const idx = Math.trunc(n); // 0..6
    if (idx < 0 || idx >= dayOptions.length) return "";
    return dayOptions[idx];
}