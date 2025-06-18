package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "images_id_seq", name = "images_id_seq")
    private long id;

    @Column(name = "image", nullable = false)
    private byte[] image;

    public Images() {
    }

    public Images(byte[] image) {
        this.image = image;
    }

    public Images(long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
