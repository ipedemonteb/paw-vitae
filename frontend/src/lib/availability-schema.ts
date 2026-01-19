import { z } from "zod";

export const DAYS = [ "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY" ];
export const TIMES = [ "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00" ];

export const DB_DAY_TO_FORM: Record<number, string> = {
    1: "MONDAY", 2: "TUESDAY", 3: "WEDNESDAY", 4: "THURSDAY",
    5: "FRIDAY", 6: "SATURDAY", 0: "SUNDAY"
};

export const FORM_DAY_TO_DB: Record<string, number> = {
    "MONDAY": 1, "TUESDAY": 2, "WEDNESDAY": 3, "THURSDAY": 4,
    "FRIDAY": 5, "SATURDAY": 6, "SUNDAY": 0
};

export const extractIdFromUrl = (url?: string): string => {
    if (!url) return "";
    const segments = url.split('/').filter(Boolean);
    return segments.pop() || "";
};

const timeToMinutes = (time: string) => {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
};

const scheduleItemSchema = z.object({
    officeId: z.string().min(1, "Requerido"),
    dayOfWeek: z.string().min(1, "Requerido"),
    startTime: z.string().min(1, "Requerido"),
    endTime: z.string().min(1, "Requerido"),
}).refine((data) => {
    if (!data.startTime || !data.endTime) return true;
    return timeToMinutes(data.endTime) > timeToMinutes(data.startTime);
}, {
    message: "errorTimeOrder",
    path: ["endTime"],
});

export const formSchema = z.object({
    schedules: z.array(scheduleItemSchema).superRefine((items, ctx) => {
        items.forEach((item, index) => {
            const currentStart = timeToMinutes(item.startTime);
            const currentEnd = timeToMinutes(item.endTime);
            items.forEach((otherItem, otherIndex) => {
                if (index === otherIndex) return;

                if (item.dayOfWeek === otherItem.dayOfWeek) {
                    const otherStart = timeToMinutes(otherItem.startTime);
                    const otherEnd = timeToMinutes(otherItem.endTime);

                    const hasOverlap = (currentStart < otherEnd) && (currentEnd > otherStart);

                    if (hasOverlap) {
                        ctx.addIssue({
                            code: z.ZodIssueCode.custom,
                            message: "errorOverlap",
                            path: [index, "startTime"]
                        });
                    }
                }
            });
        });
    }),
});

export type AvailabilityFormValues = z.infer<typeof formSchema>;