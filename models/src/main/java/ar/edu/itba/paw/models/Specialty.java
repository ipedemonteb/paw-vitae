package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specialties_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "specialties_id_seq", name = "specialties_id_seq")
    private long id;

    @Column(name = "key", nullable = false)
    private String key;

    public Specialty() {
        // For Hibernate use
    }

    public Specialty(long id, String key) {
        this.id = id;
        this.key = key;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return id == specialty.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
