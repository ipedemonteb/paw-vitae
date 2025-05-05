package ar.edu.itba.paw.models;

public class Patient extends User {

    private Coverage coverage;

    public Patient(String name, long id, String lastName, String email, String password, String phone, String language, Coverage coverage, boolean verified) {
        super(name, id, lastName, email, password, phone, language, verified);
        this.coverage = coverage;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }
}
