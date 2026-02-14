import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

const ghostFilter = "h-0 sm:h-9";
const containerStyles = "flex flex-col gap-6 max-w-6xl mx-auto w-full mb-2";

type DashboardRefetchProps = {
    title: string;
    text: string;
    isFetching: boolean;
    refetch: () => Promise<unknown>;
};

export function DashboardRefetch({ title, text, isFetching, refetch }: DashboardRefetchProps) {
    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={title}>
                <div className={ghostFilter} />
            </DashboardNavHeader>
            <div className={containerStyles}>
                <RefetchComponent
                    isFetching={isFetching}
                    onRefetch={() => refetch()}
                    errorText={text}
                    className="w-full flex justify-center h-40"
                />
            </div>
        </DashboardNavContainer>
    );
}