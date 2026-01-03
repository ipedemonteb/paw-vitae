import {Clock4, EyeIcon} from 'lucide-react'
import {Button} from "@/components/ui/button.tsx";

const statusClassname = " h-7 font-medium border-solid border text-xs w-3/4 rounded-2xl flex items-center justify-center"
const confirmedStatusClassname = statusClassname + " bg-green-100 border-green-400"
const cancelledStatusClassname = statusClassname + " bg-red-100 border-red-400"
const completedStatusClassname = statusClassname + " bg-blue-100 border-blue-400"

const statusDictionary = {
    completed: completedStatusClassname,
    confirmed: confirmedStatusClassname,
    cancelled: cancelledStatusClassname
}

export default function AppointmentCard() {
    return (
        <div className="max-w-200 h-55 border border-solid rounded-md flex shadow mt-10 mx-5">
            <div className="bg-gray-100 h-full w-1/6 space-y-3 flex flex-col justify-center items-center">
                <div className="text-sm flex flex-col space-y-0 gap-0 items-center justify-center text-center w-full">
                    <span className="text-(--text-light) ">
                        TUESDAY
                    </span>
                    <span className="text-3xl font-bold">
                        30
                    </span>
                    <span className="text-(--text-light)">
                        DECEMBER
                    </span>

                </div>
                <div className="w-full gap-x-1.5 flex flex-row justify-center items-center">
                    <Clock4 color="var(--primary-color)" className="size-4"/> 14:00
                </div>
                <div className="w-full flex justify-center items-center opacity-80">
                    <div className={statusDictionary["confirmed"]}>
                        Confirmed
                    </div>
                </div>
            </div>
            <div className="h-full px-5 w-5/6 flex flex-col items-center justify-center">
                <div className=" flex gap-3 h-1/3 w-full items-center justify-baseline">
                    <div className="rounded-full border border-solid text-blue-700 border-blue-500 flex justify-center items-center bg-blue-200 h-10 w-10">
                        JB
                    </div>
                    <div className="flex flex-col">
                        <span className="font-semibold">
                            Paciente Prueba
                        </span>
                        <span className="text-(--text-light) text-xs flex felx-row items-center justify-baseline gap-2">
                            <span className="bg-blue-500 w-2 h-2 rounded-full"></span>
                            Galeno
                        </span>
                    </div>
                </div>
                <div className=" flex h-1/3 w-full py-2">
                    <div className="h-full flex w-full">
                        <div className="w-3 bg-blue-600 h-full rounded-sm">

                        </div>
                        <div className="min-w-0 rounded-sm -ml-2 h-full w-full text-xs bg-gray-50 flex flex-col px-3 justify-center">
                            <span className="text-(--text-light)">
                                Reason for Visit:
                            </span>
                            <div className="relative w-full">
                                <span className="truncate block w-full">
                                    Very long textVery long textVery long textVery long textVery long textVery long textVery long textVery long textVery long textVery long text
                                </span>
                                {/* chiche magico para que fadee el texto a la derecha cuando se pasa */}
                                <div className="pointer-events-none absolute top-0 right-0 h-full w-8 bg-linear-to-r from-transparent to-gray-50"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="flex justify-between items-center h-1/3 w-full">
                    <div className="rounded-2xl font-medium text-(--primary-color) h-min py-1.5 px-2.5 text-sm flex items-center justify-center bg-gray-100">
                        General
                    </div>
                    <Button className="flex gap-2 hover:bg-(--primary-dark) cursor-pointer items-center rounded-lg text-white justify-center px-2 py-2 bg-(--primary-color)">
                        <EyeIcon/>
                        View Details
                    </Button>
                </div>

            </div>
        </div>
    );
}