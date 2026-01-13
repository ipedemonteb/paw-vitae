export function toBackendDayOfWeek(date: Date): number {
    const js = date.getDay();
    return (js + 6) % 7;
}

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