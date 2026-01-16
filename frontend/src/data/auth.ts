import {api, NEW_JWT_HEADER, NEW_REFRESH_HEADER} from "@/data/Api.ts"
import {t} from "i18next";
import {ContentTypes} from "@/utils/contentTypes.ts";
import type {ChangePasswordForm} from "@/data/patients.ts";

export async function login(email: string, password: string) {
    const credentials = btoa(email + ":" + password);
    const res = await api.head("/", {
        headers: { Authorization: `Basic ${credentials}` }
    });

    const jwt = res.headers[NEW_JWT_HEADER];
    const refresh = res.headers[NEW_REFRESH_HEADER];

    if (!jwt || !refresh) {
        throw new Error(t("login.error_credentials"));
    }

    return { jwt, refresh };
}

export async function changePassword(url: string, form: ChangePasswordForm) {
    const res = await api.patch(`${url}`,form,{
        headers: {"Content-Type":  ContentTypes.USER_PASSWORD }
    });
    return res.data;
}