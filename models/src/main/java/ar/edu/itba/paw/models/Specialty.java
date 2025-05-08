package ar.edu.itba.paw.models;

import java.util.Objects;

public class Specialty {

    private final long id;
    private String key;

    public Specialty(long id, String key) {
        this.id = id;
        this.key = key;
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
