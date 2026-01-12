import {Button} from "@/components/ui/button.tsx";
import {ChevronLeft, ChevronRight} from 'lucide-react'


export default function PageNavigation() {
    return (
        <div className="flex gap-2 justify-center items-center">
            <ChevronLeft size={36} className="hover:bg-gray-100 cursor-pointer rounded-sm p-2" />
            <Button className="cursor-pointer bg-(--primary-color) hover:bg-(--primary-dark) ">
                1
            </Button>
            <Button className="cursor-pointer bg-(--primary-color) hover:bg-(--primary-dark)  ">
                2
            </Button>
            <Button className="cursor-pointer bg-(--primary-color) hover:bg-(--primary-dark)  ">
                3
            </Button>
            <ChevronRight size={36} className="hover:bg-gray-100 cursor-pointer rounded-sm p-2" />
        </div>
    )
}