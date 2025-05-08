package ar.edu.itba.paw.models;

import java.util.Objects;

public class Coverage {

    private final long id;
    private String name;

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
