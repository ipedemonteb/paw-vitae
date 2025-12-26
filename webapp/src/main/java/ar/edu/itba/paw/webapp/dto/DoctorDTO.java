package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Doctor;

public class DoctorDTO {
    private String name;

    public static DoctorDTO fromDoctor(Doctor doctor) {
        DoctorDTO res = new DoctorDTO();
        res.name = doctor.getName();
        return res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
