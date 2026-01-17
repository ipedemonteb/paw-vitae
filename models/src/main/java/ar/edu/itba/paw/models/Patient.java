package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "patient_id")
public class Patient extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverage_id")
    private Coverage coverage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;


    public Patient() {
    }

    public Patient(String name, String lastName, String email, String password, String phone, String language, Coverage coverage, Neighborhood neighborhood, boolean verified) {
        super(name, lastName, email, password, phone, language, verified);
        this.coverage = coverage;
        this.neighborhood = neighborhood;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coverage, neighborhood);
    }
}
