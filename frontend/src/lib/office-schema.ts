import { z } from "zod";

export const EditOfficeSchema = z.object({
    name: z.string().min(1, "Office name is required"),
    neighborhood: z.string().optional(),
    active: z.boolean(),
    specialties: z.array(z.string()),
    removed: z.boolean().optional()
});

export type EditOfficeForm = z.infer<typeof EditOfficeSchema>;