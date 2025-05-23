package ar.edu.itba.paw.models;

public class DoctorOfficeForm {
    private Long neighborhoodId;
    private String officeName;

    public DoctorOfficeForm(Long neighborhoodId, String officeName) {
        this.neighborhoodId = neighborhoodId;
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

    public DoctorOffice toEntity(Doctor doctor, Neighborhood neighborhood) {
        DoctorOffice doctorOffice = new DoctorOffice();
        doctorOffice.setDoctor(doctor);
        doctorOffice.setNeighborhood(neighborhood);
        doctorOffice.setId(new DoctorOfficeId(doctor.getId(), neighborhoodId, officeName));
        return doctorOffice;
    }

    public DoctorOfficeForm fromEntity(DoctorOffice doctorOffice) {
        return new DoctorOfficeForm(
                doctorOffice.getNeighborhood().getId(),
                doctorOffice.getOfficeName()
        );
    }
}
