import {type RecoverPasswordForm, recoverPassword} from "@/data/user.ts";
import {useMutation, useQuery} from "@tanstack/react-query";
import {ROLE_DOCTOR, ROLE_PATIENT} from "@/lib/constants.ts";
import {getPatientById} from "@/data/patients.ts";
import {getDoctor} from "@/data/doctors.ts";

type User = {
    name: string;
    lastName: string
}

export function useUserMutation() {
    return useMutation({
        mutationFn: (form: RecoverPasswordForm) => recoverPassword(form),
    });
}

export function useUser(role?: string, id?: string) {
    const enabled = !!role && !!id;

    return useQuery<User, Error>({
        queryKey: ["user", role, id],
        enabled,
        queryFn: async () => {
            if (!role || !id) throw new Error("Missing role or id");

            if (role === ROLE_DOCTOR) return getDoctor(id);
            if (role === ROLE_PATIENT) return getPatientById(id);

            throw new Error("Invalid role");
        },
    });
}