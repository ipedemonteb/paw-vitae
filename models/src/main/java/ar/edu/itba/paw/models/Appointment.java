package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointments_id_seq")
    @SequenceGenerator(name = "appointments_id_seq", sequenceName = "appointments_id_seq", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

    @Column
    private String reason;

    @Column(name = "allow_full_history", nullable = false)
    private boolean allowFullHistory = true;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "office_id", nullable = false)
    private DoctorOffice doctorOffice;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentFile> appointmentFiles;
    @Column
    private String report;

    //TODO: revisar esto?
    @OneToOne(mappedBy = "appointment", optional = true)
    private Rating rating;

    @Transient
    private Boolean cancellable = true;

    public Appointment() {}

    public Appointment(LocalDateTime date, String status, String reason, Specialty specialty, Doctor doctor, Patient patient, String report, DoctorOffice doctorOffice, boolean allowFullHistory) {
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.specialty = specialty;
        this.doctor = doctor;
        this.patient = patient;
        this.report = report;
        this.doctorOffice = doctorOffice;
        this.allowFullHistory = allowFullHistory;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Boolean getCancellable() {
        return cancellable;
    }

    public void setCancellable(Boolean cancellable) {
        this.cancellable = cancellable;
    }

    public DoctorOffice getDoctorOffice() {
        return doctorOffice;
    }

    public void setDoctorOffice(DoctorOffice doctorOffice) {
        this.doctorOffice = doctorOffice;
    }

    public boolean isAllowFullHistory() {
        return allowFullHistory;
    }

    public void setAllowFullHistory(boolean allowFullHistory) {
        this.allowFullHistory = allowFullHistory;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, status, reason, allowFullHistory, (long) appointmentFiles.size());
    }


}
