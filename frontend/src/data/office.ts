export interface OfficeDTO {
    name: string;
    status: "active" | "inactive"
    doctor: string;
    neighborhood: string;
    officeAvailability: string;
    officeSpecialties: string;
    self: string;
}