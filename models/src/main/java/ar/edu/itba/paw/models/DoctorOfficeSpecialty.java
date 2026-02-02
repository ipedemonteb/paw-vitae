package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
@Table(name = "doctor_office_specialties")
public class DoctorOfficeSpecialty {

    @EmbeddedId
    private DoctorOfficeSpecialtyId id;

    @MapsId("officeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id", nullable = false)
    @JsonView(Views.Private.class)
    private DoctorOffice office;

    @MapsId("specialtyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    public DoctorOfficeSpecialty() {}

    public DoctorOfficeSpecialty(DoctorOffice office, Specialty specialty) {
        this.office = office;
        this.specialty = specialty;
    }

    public static class Views {
        public static class Public {}
        public static class Private extends Doctor.Views.Public {}
    }

    public DoctorOfficeSpecialtyId getId() {
        return id;
    }

    public DoctorOffice getOffice() {
        return office;
    }

    public void setOffice(DoctorOffice office) {
        this.office = office;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
