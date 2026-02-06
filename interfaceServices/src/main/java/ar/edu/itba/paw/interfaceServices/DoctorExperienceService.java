package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.ExperienceForm;

import java.time.LocalDate;
import java.util.List;

public interface DoctorExperienceService {

     DoctorExperience create(long doctorId, String title, String orgName, LocalDate startDate, LocalDate endDate);

     List<DoctorExperience> findByDoctorId(long doctorId);

     void update(Doctor doctor, List<ExperienceForm> experiences);
}
