package ar.edu.itba.paw.models;

import javax.swing.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Doctor {
    private final long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private List<String> specialty;
    private Array coverageIds;
    private List<Appointment> appointments;
    private List<String> addresses;
    public Doctor(long id, String name, String email, String password, String phone,List<String> specialty,Array coverage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.specialty = specialty;
        this.coverageIds = coverage;
    }

    public long getId() {
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

    public List<String> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<String> specialty) {
        this.specialty = specialty;
    }

    public Array getCoverage() {
        return coverageIds;
    }

    public void setCoverage(Array coverage) {
        this.coverageIds = coverage;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<String> getAddresses() {
        return addresses;
    }

}