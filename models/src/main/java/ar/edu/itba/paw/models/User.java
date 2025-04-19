package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String name;
    private final long id;
    private String lastName;
    private final String email;
    private String password;
    private String phone;
    private String language;
    private List<Appointment> appointments = new ArrayList<>();

    public User(String name, long id, String lastName, String email, String password, String phone, String language) {
        this.name = name;
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.language = language;
    }


    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
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

    public String getLanguage() { return language; }
}
