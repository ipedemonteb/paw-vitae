import { RotateCcw } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { useTranslation } from "react-i18next";

type RefetchStateProps = {
    isFetching?: boolean;
    onRefetch: () => void;

    errorText?: string;
    tryAgainText?: string;

    className?: string;
    buttonClassName?: string;
    spinnerClassName?: string;

    disabled?: boolean;
    hideIcon?: boolean;
};

const defaultWrap = "flex flex-col items-center text-center";
const buttonStyling = "text-(--gray-600)";
const buttonLogo = "text-(--gray-600) size-4";
const defaultButton = "shadow-none cursor-pointer";

export function RefetchComponent({
                                     isFetching = false,
                                     onRefetch,
                                     errorText,
                                     tryAgainText,

                                     className,
                                     buttonClassName,
                                     spinnerClassName,

                                     disabled,
                                     hideIcon = false,
                                 }: RefetchStateProps) {
    const { t } = useTranslation();

    const msg = errorText ?? t("error.default.message");
    const cta = tryAgainText ?? t("error.try-again");

    const isDisabled = (disabled ?? false) || isFetching;

    return (
        <div className={`${defaultWrap} ${className ?? ""}`}>
            <p className="text-(--danger) mb-4">{msg}</p>

            <Button
                variant="outline"
                onClick={onRefetch}
                className={`${defaultButton} ${buttonClassName ?? ""}`}
                disabled={isDisabled}
                aria-busy={isFetching}
            >
                {isFetching ? (
                    <>
                        <Spinner className={`${buttonLogo} ${spinnerClassName ?? ""}`} />
                        <p className={buttonStyling}>{t("loading")}</p>
                    </>
                ) : (
                    <>
                        {!hideIcon && <RotateCcw className={buttonLogo} />}
                        <p className={buttonStyling}>{cta}</p>
                    </>
                )}
            </Button>
        </div>
    );
}
