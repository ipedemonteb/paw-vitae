import OfficeCard from "@/components/OfficeCard.tsx";
import {Input} from "@/components/ui/input.tsx";
import NeighborhoodCombobox from "@/components/NeighborhoodCombobox.tsx";
import type {OfficeDTO} from "@/data/office.ts";
import {useDoctorOfficeSpecialties} from "@/hooks/useDoctors.ts";
import SpecialtyToggleGroup from "@/components/SpecialtyToggleGroup.tsx";
import {useState} from "react";
import {Button} from "@/components/ui/button.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useSpecialties, useSpecialtiesByUrl} from "@/hooks/useSpecialties.ts";
import {Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTrigger} from "@/components/ui/dialog";
import {Label} from "@/components/ui/label.tsx";

type EditOfficeModalProps = {
    office: OfficeDTO
}

export default function EditOfficeDialog({office}: EditOfficeModalProps) {
    const {data: officeSpecialties, isLoading} = useDoctorOfficeSpecialties(office.officeSpecialties)
    const {data: specialties, isLoading: isLoadingSpecialties} = useSpecialties()
    const {data: currentSpecialties, isLoading: isLoadingCurrentSpecialties} = useSpecialtiesByUrl(officeSpecialties?.map(s => s.specialty))
    const [value, setValue] = useState(office.name)
    return (
        <Dialog onOpenChange={() => setValue(office.name)}>
            <DialogTrigger asChild >
                <OfficeCard office={office}/>
            </DialogTrigger>
            <DialogContent className="max-w-155! p-6 overflow-x-auto">
                <form onSubmit={(event) => {
                    event.preventDefault()
                }}>
                    <DialogHeader>
                        <div className="border-b w-full relative pb-2 flex items-center gap-1 text-lg font-medium">
                            Edit Office
                        </div>
                        <div className="flex flex-col items-center gap-2.5">
                            <div className="flex justify-between w-full items-center gap-4">
                                <div className="flex flex-col gap-1.5 text-(--text-light) text-[1rem] ">
                                    <Label htmlFor="name" className="pl-1">Office Name</Label>
                                    <Input id="name" value={value} onInput={(event) => setValue(event.currentTarget.value)} className=" text-black w-70" type="text"/>
                                </div>
                                <div className="flex flex-col gap-1.5 text-(--text-light) text-[1rem]">
                                    <Label className="pl-1">Neighborhood</Label>
                                    <NeighborhoodCombobox currentNeighborhood={office.neighborhood} />
                                </div>
                            </div>
                            <div className="w-full flex flex-col gap-2">
                                <Label className="text-[1rem] text-(--text-light)">Specialties</Label>
                                {(isLoading || isLoadingCurrentSpecialties || isLoadingSpecialties) ? (
                                    <div className="w-full flex flex-wrap gap-y-1 gap-x-0.5 ">
                                        {Array.from({length: 15}).map((_, i) => (
                                            <Skeleton key={i} className="w-26 h-7 rounded-full"/>
                                        ))}
                                    </div>
                                ) : (
                                    <SpecialtyToggleGroup currentSpecialties={currentSpecialties} specialties={specialties} />
                                )}

                            </div>
                        </div>
                    </DialogHeader>
                    <DialogFooter className="pt-4">
                        <DialogClose className=" hover:bg-gray-100 rounded-md py-1 px-3 text-sm bg-white text-(--text-light) border cursor-pointer">
                            Cancel
                        </DialogClose>
                        <Button type="submit" className="border-none hover:bg-(--primary-dark)  bg-(--primary-color) text-white cursor-pointer">
                            Save Changes
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    )
}