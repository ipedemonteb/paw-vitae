import {ClipboardPenIcon} from 'lucide-react'

export default function DashboardNavEmptyContent() {
    return (
        <div className="w-full flex flex-col gap-2 items-center rounded-2xl justify-center h-72 m-2 bg-gray-100">
            <ClipboardPenIcon size={70} className="text-(--text-light)"/>
        </div>
    )
}