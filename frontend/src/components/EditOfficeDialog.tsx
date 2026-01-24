import OfficeCard from "@/components/OfficeCard.tsx";
import {Input} from "@/components/ui/input.tsx";
import NeighborhoodCombobox from "@/components/NeighborhoodCombobox.tsx";
import type {OfficeDTO} from "@/data/office.ts";
import {useDoctorOfficeSpecialties, useUpdateOfficeMutation} from "@/hooks/useDoctors.ts";
import SpecialtyToggleGroup from "@/components/SpecialtyToggleGroup.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useSpecialties, useSpecialtiesByUrl} from "@/hooks/useSpecialties.ts";
import {Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTrigger} from "@/components/ui/dialog";
import {Label} from "@/components/ui/label.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {useForm} from "react-hook-form";
import {type EditOfficeForm, EditOfficeSchema} from "@/lib/office-schema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {useEffect, useState} from "react";
import {specialtyIdFromSelf} from "@/utils/IdUtils.ts";
import {extractIdFromUrl} from "@/lib/utils.ts";
import {toast} from "sonner";

type EditOfficeModalProps = {
    office: OfficeDTO
}

export default function EditOfficeDialog({office}: EditOfficeModalProps) {
    const {data: officeSpecialties, isLoading} = useDoctorOfficeSpecialties(office.officeSpecialties)
    const {data: specialties, isLoading: isLoadingSpecialties} = useSpecialties()
    const {data: currentSpecialties, isLoading: isLoadingCurrentSpecialties} = useSpecialtiesByUrl(officeSpecialties?.map(s => s.specialty))
    const [open, setOpen] = useState(false)

    const form = useForm<EditOfficeForm>({
        resolver: zodResolver(EditOfficeSchema),
        defaultValues: {
            name: office.name,
            neighborhood: office.neighborhood,
            active: office.status === "active",
            specialties: [] as string[],
        },
        mode: "onSubmit",
    })

    const updateOfficeMutation = useUpdateOfficeMutation(office.self)


    useEffect(() => {
        if (!isLoadingCurrentSpecialties && currentSpecialties) {
            form.reset({
                name: office.name,
                neighborhood: office.neighborhood,
                active: office.status === "active",
                specialties: currentSpecialties
                    .filter(s => s.data !== undefined)
                    .map(s => s.data.self),
            });
        }
    }, [
        isLoadingCurrentSpecialties,
        currentSpecialties,
        office,
        form
    ])

    const onSubmit = form.handleSubmit((values) => {
        const parsed = EditOfficeSchema.safeParse(values);
        if (parsed.error) {
            toast.error("An Error Occurred")
            return;
        }
        const specialties = values.specialties.map(s => specialtyIdFromSelf(s)).filter(s => s !== null)
        const neighborhood = extractIdFromUrl(values.neighborhood)
        updateOfficeMutation.mutate({
            officeName: values.name,
            specialtyIds: specialties,
            neighborhoodId: neighborhood,
            active: values.active,
            removed: false
        }, {
            onSuccess: () => {
                toast.success("Office Updated")
            },
            onError: () => {
                toast.error("An Error Occurred")
            },
            onSettled: () => {
                setOpen(false)
            }
        })
    })

    return (
        <Dialog open={open} onOpenChange={(open) => {
            if (open) form.reset()
            setOpen(open)
        }}>
            <DialogTrigger asChild >
                <OfficeCard office={office}/>
            </DialogTrigger>
            <DialogContent className="max-w-155! p-5 overflow-x-auto">
                <form onSubmit={(event) => {
                    event.preventDefault()
                    onSubmit()
                }}>
                    <DialogHeader>
                        <div className="border-b w-full relative pb-2 flex items-center gap-1 text-lg font-medium">
                            Edit Office
                        </div>
                        <div className="flex flex-col items-center gap-2.5">
                            <div className="flex justify-between w-full items-center gap-4">
                                <div className="flex flex-col gap-1.5 text-(--text-light)  ">
                                    <Label htmlFor="name" className="pl-1 text-[1rem]">Office Name</Label>
                                    <Input
                                        id="name"
                                        className=" text-black w-70 selection:bg-blue-300 selection:text-black" type="text"
                                        {...form.register("name")}
                                    />
                                </div>
                                <div className="flex flex-col gap-1.5 text-(--text-light) text-[1rem]">
                                    <Label className="pl-1 text-[1rem]">Neighborhood</Label>
                                    <NeighborhoodCombobox
                                        value={form.watch("neighborhood")}
                                        onChange={(val) => form.setValue("neighborhood", val, { shouldDirty: true })}
                                    />
                                </div>
                            </div>
                            <div className="w-full flex flex-col gap-1 ">
                                <Label htmlFor="active" className="text-[1rem] text-(--text-light)">Active</Label>
                                <Switch  defaultChecked={form.watch("active")} id="active" className="data-[state=checked]:bg-green-400 data-[state=checked]: transition-all " />
                            </div>
                            <div className="w-full flex flex-col gap-1.5">
                                <Label className="text-[1rem] text-(--text-light)">Specialties</Label>
                                {(isLoading || isLoadingCurrentSpecialties || isLoadingSpecialties) ? (
                                    <div className="w-full flex flex-wrap gap-y-1 gap-x-0.5 ">
                                        {Array.from({length: 15}).map((_, i) => (
                                            <Skeleton key={i} className="w-28 h-8 rounded-full"/>
                                        ))}
                                    </div>
                                ) : (
                                    <SpecialtyToggleGroup onValueChange={(s: string[]) => form.setValue("specialties", s)} currentSpecialties={form.watch("specialties")} specialties={specialties} />
                                )}
                            </div>
                        </div>
                    </DialogHeader>
                    <DialogFooter className="pt-4">
                        <DialogClose className="  hover:bg-gray-100 rounded-md py-1 px-3 text-sm bg-white text-(--text-light) border cursor-pointer">
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