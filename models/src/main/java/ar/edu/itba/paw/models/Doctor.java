package ar.edu.itba.paw.models;

import javax.swing.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Doctor extends User {

    private List<Specialty> specialtyList;
    private List<Coverage> coverageList = new ArrayList<>();
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();

    public Doctor(String name, long id, String lastName, String email, String password, String phone, List<Specialty> specialty, List<Coverage> coverageList, List<AvailabilitySlot> availabilitySlots) {
        super(name, id, lastName, email, password, phone);
        this.specialtyList = specialty;
        this.coverageList = coverageList;
        this.availabilitySlots = availabilitySlots;
    }

    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public List<Coverage> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(List<Coverage> coverageList) {
        this.coverageList = coverageList;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }
    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

}