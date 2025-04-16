package ar.edu.itba.paw.models;

public class Client extends User{

    private Coverage coverage;

    public Client(String name, long id, String lastName, String email, String password, String phone, String language, Coverage coverage) {
        super(name, id, lastName, email, password, phone, language);
        this.coverage = coverage;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

}
