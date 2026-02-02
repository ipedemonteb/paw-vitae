import { z } from "zod";

//TODO internacionalizar, jeje

export const EditOfficeSchema = z.object({
    name: z.string().min(1, "Office name is required"),
    neighborhood: z.string(),
    active: z.boolean(),
    specialties: z.array(z.string()).min(1, "At least one specialty is required"),
});

export const CreateOfficeSchema = z.object({
    name: z.string().min(1, "Office name is required"),
    neighborhood: z.string(),
    specialties: z.array(z.string()).min(1, "At least one specialty is required"),
});


export type EditOfficeForm = z.infer<typeof EditOfficeSchema>;
export type CreateOfficeForm = z.infer<typeof CreateOfficeSchema>;