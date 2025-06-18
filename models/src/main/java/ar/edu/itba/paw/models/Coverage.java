package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "coverages")
public class Coverage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coverages_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "coverages_id_seq", name = "coverages_id_seq")
    private Long id;

    @Column(name = "coverage_name", nullable = false)
    private String name;

    public Coverage() {
    }

    public Coverage(String name) {
        this.name = name;
    }

    public Coverage(long id, String name) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coverage coverage = (Coverage) o;
        return id == coverage.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
