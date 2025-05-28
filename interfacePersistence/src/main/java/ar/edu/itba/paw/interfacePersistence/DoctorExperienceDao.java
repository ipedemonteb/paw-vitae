package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorExperienceDao {

     DoctorExperience create(Doctor doctor, String title, String description, LocalDate startDate, LocalDate endDate);

     List<DoctorExperience> getByDoctorId(long doctorId);
}
