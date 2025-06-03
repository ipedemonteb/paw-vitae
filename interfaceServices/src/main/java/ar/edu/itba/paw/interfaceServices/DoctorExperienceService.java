package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.ExperienceForm;

import java.time.LocalDate;
import java.util.List;

public interface DoctorExperienceService {

    public DoctorExperience create(long doctorId, String title, String orgName, LocalDate startDate, LocalDate endDate);

    public DoctorExperience findByDoctorId(long doctorId);

    public void update(Doctor doctor, List<ExperienceForm> experiences);
}
