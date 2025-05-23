package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "doctor_offices")
public class DoctorOffice {

    @EmbeddedId
    private DoctorOfficeId id;

    @MapsId("doctorId")   // tells JPA which part of the PK this maps to
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @MapsId("neighborhoodId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;

    // no separate field for officeName; access via id.getOfficeName()

    public DoctorOffice() { }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, String officeName) {
        this.id = new DoctorOfficeId(
                doctor.getId(),
                neighborhood.getId(),
                officeName
        );
        this.doctor = doctor;
        this.neighborhood = neighborhood;
    }

    public String getOfficeName() {
        return id.getOfficeName();
    }

    public DoctorOfficeId getId() {
        return id;
    }

    public void setId(DoctorOfficeId id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }
}
