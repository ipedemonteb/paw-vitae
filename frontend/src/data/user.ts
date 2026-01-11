import { api } from "@/data/Api.ts";
import { ContentTypes } from "@/utils/contentTypes.ts";
import type { AxiosRequestConfig } from "axios";

export type RecoverPasswordForm = {
    email: string;
};

export async function recoverPassword(form: RecoverPasswordForm) {
    await api.post("/users", form, {
        headers: {
            "content-type": ContentTypes.USER_PASSWORD,
        },
    } as AxiosRequestConfig);
}