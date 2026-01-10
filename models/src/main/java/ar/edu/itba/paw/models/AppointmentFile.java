package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "appointment_files")
public class AppointmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_files_id_seq")
    @SequenceGenerator(name = "appointment_files_id_seq", sequenceName = "appointment_files_id_seq", allocationSize = 1)
    private long  id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "uploader_role", nullable = false)
    private String uploaderRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    public AppointmentFile() {}

    public AppointmentFile(String fileName, byte[] fileData, String uploaderRole, Appointment appointment) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.uploaderRole = uploaderRole;
        this.appointment = appointment;
    }
    public AppointmentFile(long id, String fileName, String uploaderRole, Appointment appointment) {
        this.id = id;
        this.fileName = fileName;
        this.uploaderRole = uploaderRole;
        this.appointment = appointment;
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getUploaderRole() {
        return uploaderRole;
    }

    public void setUploaderRole(String uploaderRole) {
        this.uploaderRole = uploaderRole;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
