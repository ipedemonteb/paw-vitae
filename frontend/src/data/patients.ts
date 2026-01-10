import {api} from "@/data/Api.ts";
import axios from "axios";
import { ContentTypes } from "@/utils/contentTypes.js";
export type PatientDTO = {
    name: string;
    lastName: string;
    email: string;
    phone: string;

    coverages: string;
    neighborhood: string;
    appointments: string;
    self: string
}
export type ChangePasswordForm = {
    password: string;
    repeatedPassword: string;

}

export async function getPatient(url: string) {
    const res = await api.get<PatientDTO>(url)
    return res.data;
}

export async function changePatientPassword(url: string, form: ChangePasswordForm) {
    const res = await api.patch(`${url}`,form,{
        headers: {
           "content-type":  ContentTypes.USER_PASSWORD
        }
    } as axios.AxiosRequestConfig);
    return res.data;
}