package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorProfile;

public interface DoctorProfileService {

    public DoctorProfile create(long doctorId, String bio, String description);

    public DoctorProfile findByDoctorId(long id);
}
