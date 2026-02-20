import { z } from "zod";
import type { TFunction } from "i18next";


export const getCreateOfficeSchema = (t: TFunction) => z.object({
    name: z.string().min(1, t("offices.validation.name_required")).max(50, t("offices.validation.name_max")),

    neighborhood: z.string().min(1, t("offices.validation.neighborhood_required")),

    specialties: z.array(z.string()).min(1, t("offices.validation.specialty_required")),
});

export const getEditOfficeSchema = (t: TFunction) => getCreateOfficeSchema(t).extend({
    active: z.boolean()
});

export type CreateOfficeForm = z.infer<ReturnType<typeof getCreateOfficeSchema>>;
export type EditOfficeForm = z.infer<ReturnType<typeof getEditOfficeSchema>>;