package ar.edu.itba.paw.models;

public class Images {

    private final  long id;
    private byte[] image;

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public Images( long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

}
