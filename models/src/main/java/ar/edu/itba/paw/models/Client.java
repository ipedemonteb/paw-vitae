package ar.edu.itba.paw.models;

public class Client {

    private final String id;
    private String coverageId;
    private String coverage;
    private String name;
    private String email;
    private String password;
    private String phone;

    public Client(String id, String coverageId, String coverage, String name, String email, String password, String phone) {
        this.id = id;
        this.coverageId = coverageId;
        this.coverage = coverage;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public String getCoverageId() {
        return coverageId;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
