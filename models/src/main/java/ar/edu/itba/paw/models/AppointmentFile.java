package ar.edu.itba.paw.models;

public class AppointmentFile {
    private String fileName;
    private byte[] fileData;
    private long id;
    private String uploader_role;
    private long appointment_id;
    public AppointmentFile(String fileName, byte[] fileData, long id, String uploader_role, long appointment_id) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.id = id;
        this.uploader_role = uploader_role;
        this.appointment_id = appointment_id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUploader_role() {
        return uploader_role;
    }

    public void setUploader_role(String uploader_role) {
        this.uploader_role = uploader_role;
    }

    public long getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(long appointment_id) {
        this.appointment_id = appointment_id;
    }
}
