import {Spinner} from "@/components/ui/spinner.tsx";

type DashboardNavLoaderProps = {
    item: string
}

export default function DashboardNavLoader({item}: DashboardNavLoaderProps) {
    return (
        <div className="w-full flex flex-col gap-2 items-center rounded-2xl justify-center h-72 m-2 bg-gray-100">
            <Spinner className="h-8 w-8 text-(--text-light)"/>
            <span className="text-(--text-light)">
                Fetching {item}...
            </span>
        </div>
    )
}