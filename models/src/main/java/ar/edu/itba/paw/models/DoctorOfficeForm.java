package ar.edu.itba.paw.models;

import java.util.List;

public class DoctorOfficeForm {
    private Long id;
    private Long neighborhoodId;
    private String officeName;
    private List<Long> specialtyIds;
    private boolean active;
    private boolean removed;

    private List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms;

    public DoctorOfficeForm(Long id, Long neighborhoodId, List<Long> specialtyIds, String officeName, List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms, boolean active, boolean removed) {
        this.id = id;
        this.neighborhoodId = neighborhoodId;
        this.specialtyIds = specialtyIds;
        this.officeName = officeName;
        this.officeAvailabilitySlotForms = officeAvailabilitySlotForms;
        this.active = active;
        this.removed = removed;
    }

    public DoctorOfficeForm(Long neighborhoodId, String officeName, List<Long> specialtyIds, List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms) {
        this.neighborhoodId = neighborhoodId;
        this.officeName = officeName;
        this.specialtyIds = specialtyIds;
        this.officeAvailabilitySlotForms = officeAvailabilitySlotForms;
    }

    public DoctorOfficeForm() {
    }

    public Long getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Long neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public List<Long> getSpecialtyIds() {
        return specialtyIds;
    }

    public void setSpecialtyIds(List<Long> specialtyIds) {
        this.specialtyIds = specialtyIds;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DoctorOfficeAvailabilityForm> getOfficeAvailabilitySlotForms() {
        return officeAvailabilitySlotForms;
    }

    public void setOfficeAvailabilitySlotForms(List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms) {
        this.officeAvailabilitySlotForms = officeAvailabilitySlotForms;
    }

    public DoctorOffice toEntity(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties) {
        return new DoctorOffice(doctor, neighborhood, specialties, officeName);
    }

}
