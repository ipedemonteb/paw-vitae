import {Spinner} from "@/components/ui/spinner.tsx";

const loadingContainer = "flex flex-col gap-4 justify-center items-center h-screen";
const spinner = "h-12 w-12 text-(--gray-400)";
const loadingText = "text-xl text-(--gray-500)";

export function LoadingFullPageComponent() {
    return (
        <div className={loadingContainer}>
            <Spinner className={spinner} />
            <p className={loadingText}>Loading...</p>
        </div>
    );
}