package ar.edu.itba.paw.models;

public class Images {

    private final  long doctor_id;
    private byte[] image;

    public void setImage(byte[] image) {
        this.image = image;
    }


    public long getDoctor_id() {
        return doctor_id;
    }

    public byte[] getImage() {
        return image;
    }

    public Images( long doctor_id, byte[] image) {
        this.doctor_id = doctor_id;
        this.image = image;
    }

}
