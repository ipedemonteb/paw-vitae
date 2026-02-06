package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorProfile;

public interface DoctorProfileService {

     DoctorProfile create(long doctorId, String bio, String description);

     DoctorProfile findByDoctorId(long id);

     void update(Doctor doctor, String bio, String description);
}
