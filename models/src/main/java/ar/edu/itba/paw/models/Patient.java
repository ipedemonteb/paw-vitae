package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "patient_id")
public class Patient extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverage_id")
    private Coverage coverage;



    public Patient() {
        // For Hibernate use
    }

    public Patient(String name, String lastName, String email, String password, String phone, String language, Coverage coverage, boolean verified) {
        super(name, lastName, email, password, phone, language, verified);
        this.coverage = coverage;
    }

    //Deprecated
//    public Patient(String name, long id, String lastName, String email, String password, String phone, String language, Coverage coverage, boolean verified) {
//        super(name, id, lastName, email, password, phone, language, verified);
//        this.coverage = coverage;
//    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

}
