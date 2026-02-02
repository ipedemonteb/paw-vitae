import { z } from "zod";

export const EditOfficeSchema = z.object({
    name: z.string().min(1, "Office name is required"),
    neighborhood: z.string(),
    active: z.boolean(),
    specialties: z.array(z.string()),
});

export const CreateOfficeSchema = z.object({
    name: z.string().min(1, "Office name is required"),
    neighborhood: z.string(),
    specialties: z.array(z.string()),
});

export type EditOfficeForm = z.infer<typeof EditOfficeSchema>;
export type CreateOfficeForm = z.infer<typeof CreateOfficeSchema>;