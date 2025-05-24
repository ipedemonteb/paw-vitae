package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctor_offices")
public class DoctorOffice {

    @EmbeddedId
    private DoctorOfficeId id;

    @MapsId("doctorId")   // tells JPA which part of the PK this maps to
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @MapsId("neighborhoodId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL) //TODO find out where cascade types are necessary
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "doctor_office_specialties",
            joinColumns = {
                    @JoinColumn(name = "doctor_id"),
                    @JoinColumn(name = "neighborhood_id"),
                    @JoinColumn(name = "office_name")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "specialty_id")
            }
    )
    private List<Specialty> specialties;

    // no separate field for officeName; access via id.getOfficeName()

    public DoctorOffice() { }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties, String officeName) {
        this.id = new DoctorOfficeId(
                doctor.getId(),
                neighborhood.getId(),
                officeName
        );
        this.doctor = doctor;
        this.neighborhood = neighborhood;
        this.specialties = specialties;
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

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }
}
