package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorProfile;

import java.util.Optional;

public interface DoctorProfileDao {

    DoctorProfile create(Doctor doctor, String bio, String description);

    Optional<DoctorProfile> getByDoctorId(long id);

    void update(DoctorProfile doctorProfile);
}
