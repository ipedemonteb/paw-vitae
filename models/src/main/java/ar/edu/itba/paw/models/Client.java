package ar.edu.itba.paw.models;

public class Client extends User{

    private Coverage coverage_id;

    public Client(String name, long id, String lastName, String email, String password, String phone, Coverage coverage_id) {
        super(name, id, lastName, email, password, phone);
        this.coverage_id = coverage_id;
    }

    public Coverage getCoverage_id() {
        return coverage_id;
    }

    public void setCoverage_id(Coverage coverage_id) {
        this.coverage_id = coverage_id;
    }

}
