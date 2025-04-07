package ar.edu.itba.paw.models;

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
}
