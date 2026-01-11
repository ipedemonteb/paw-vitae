import {type RecoverPasswordForm, recoverPassword} from "@/data/user.ts";
import { useMutation } from "@tanstack/react-query";

export function useUserMutation() {
    return useMutation({
        mutationFn: (form: RecoverPasswordForm) => recoverPassword(form),
    });
}