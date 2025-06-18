package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "neighborhoods")
public class Neighborhood {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "neighborhoods_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "neighborhoods_id_seq", name = "neighborhoods_id_seq")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Neighborhood() {
    }

    public Neighborhood(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
