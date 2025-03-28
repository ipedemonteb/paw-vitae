package ar.edu.itba.paw.models;

import java.util.Set;

public class Doctor {
    private final long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Set<String> specialty;
    private Set<String> coverage;

    public Doctor(long id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    private Set<Appointment> appointments;
    private Set<String> addresses;

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

    public Set<String> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Set<String> specialty) {
        this.specialty = specialty;
    }

    public Set<String> getCoverage() {
        return coverage;
    }

    public void setCoverage(Set<String> coverage) {
        this.coverage = coverage;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<String> addresses) {
        this.addresses = addresses;
    }
    public void addAddress(String address) {
        this.addresses.add(address);
    }
    public void removeAddress(String address) {
        this.addresses.remove(address);
    }
    public void addSpecialty(String specialty) {
        this.specialty.add(specialty);
    }
    public void removeSpecialty(String specialty) {
        this.specialty.remove(specialty);
    }
    public void addCoverage(String coverage) {
        this.coverage.add(coverage);
    }
    public void removeCoverage(String coverage) {
        this.coverage.remove(coverage);
    }
    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }
    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }
}