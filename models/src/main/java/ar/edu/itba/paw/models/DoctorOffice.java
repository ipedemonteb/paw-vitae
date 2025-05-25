package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctor_offices")
public class DoctorOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_offices_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "doctor_offices_id_seq", name = "doctor_offices_id_seq")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL) //TODO find out where cascade types are necessary
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "doctor_office_specialties",
            joinColumns = {
                    @JoinColumn(name = "office_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "specialty_id")
            }
    )
    private List<Specialty> specialties;

    @Column(name = "office_name", length = 50)
    private String officeName;

    public DoctorOffice() { }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties, String officeName) {
        this.doctor = doctor;
        this.neighborhood = neighborhood;
        this.specialties = specialties;
        this.officeName = officeName;
    }

    public long getId() {
        return id;
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

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorOffice that = (DoctorOffice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
