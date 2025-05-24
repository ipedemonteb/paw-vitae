package ar.edu.itba.paw.models;

import java.util.List;

public class DoctorOfficeForm {
    private Long neighborhoodId;
    private String officeName;
    private List<Long> specialtyIds;

    public DoctorOfficeForm(Long neighborhoodId, List<Long> specialtyIds, String officeName) {
        this.neighborhoodId = neighborhoodId;
        this.specialtyIds = specialtyIds;
        this.officeName = officeName;
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

    public DoctorOffice toEntity(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties) {
        return new DoctorOffice(doctor, neighborhood, specialties, officeName);
    }
}
