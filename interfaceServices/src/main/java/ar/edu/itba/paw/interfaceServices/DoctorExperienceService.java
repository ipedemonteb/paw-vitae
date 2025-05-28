package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;

import java.time.LocalDate;

public interface DoctorExperienceService {

    public DoctorExperience create(long doctorId, String title, String orgName, LocalDate startDate, LocalDate endDate);

    public DoctorExperience findByDoctorId(long doctorId);
}
