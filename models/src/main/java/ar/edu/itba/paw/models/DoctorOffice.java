package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctor_offices")
public class DoctorOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_offices_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "doctor_offices_id_seq", name = "doctor_offices_id_seq")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "doctor_office_specialties",
            joinColumns = {
                    @JoinColumn(name = "office_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "specialty_id")
            }
    )
    private List<Specialty> specialties;

    @OneToMany(mappedBy = "office", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorOfficeAvailability> doctorOfficeAvailability;

    @Column(name = "office_name", length = 50)
    private String officeName;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "removed")
    private LocalDateTime removed;

    public DoctorOffice() { }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties, String officeName) {
        this.doctor = doctor;
        this.neighborhood = neighborhood;
        this.specialties = specialties;
        this.officeName = officeName;
    }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties, String officeName, boolean active) {
        this.doctor = doctor;
        this.neighborhood = neighborhood;
        this.specialties = specialties;
        this.officeName = officeName;
        this.active = active;
    }

    public DoctorOffice(Doctor doctor, Neighborhood neighborhood, List<Specialty> specialties, String officeName, boolean active, LocalDateTime removed, List<DoctorOfficeAvailability> doctorOfficeAvailability) {
        this.doctor = doctor;
        this.neighborhood = neighborhood;
        this.specialties = specialties;
        this.officeName = officeName;
        this.active = active;
        this.removed = removed;
    }

    public Long getId() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getRemoved() {
        return removed;
    }

    public void setRemoved(LocalDateTime removed) {
        this.removed = removed;
    }

    public List<DoctorOfficeAvailability> getDoctorOfficeAvailability() {
        return doctorOfficeAvailability;
    }

    public void setDoctorOfficeAvailability(List<DoctorOfficeAvailability> doctorOfficeAvailabilities) {
        this.doctorOfficeAvailability = doctorOfficeAvailabilities;
    }

    public void replaceAvailability(List<DoctorOfficeAvailability> newSlots) {
        this.doctorOfficeAvailability.clear();
        newSlots.forEach(s -> {
            s.setOffice(this);
            this.doctorOfficeAvailability.add(s);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorOffice that = (DoctorOffice) o;
        return Objects.equals(id, that.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, neighborhood.getId(), officeName, active, removed);
    }
}
