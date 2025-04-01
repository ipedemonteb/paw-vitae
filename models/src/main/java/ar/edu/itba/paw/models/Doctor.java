package ar.edu.itba.paw.models;

import javax.swing.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Doctor extends User {

    private String specialty;
    private List<Coverage> coverageList = new ArrayList<>();

    public Doctor(String name, long id, String lastName, String email, String password, String phone, String specialty, List<Coverage> coverageList) {
        super(name, id, lastName, email, password, phone);
        this.specialty = specialty;
        this.coverageList = coverageList;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<Coverage> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(List<Coverage> coverageList) {
        this.coverageList = coverageList;
    }

}