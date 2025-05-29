package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorProfileDao;
import ar.edu.itba.paw.interfaceServices.DoctorProfileService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorProfileServiceImpl.class);
    private final DoctorProfileDao doctorProfileDao;
    private final DoctorService doctorService;


    @Autowired
    public DoctorProfileServiceImpl(DoctorProfileDao doctorProfileDao, DoctorService doctorService ) {
        this.doctorProfileDao = doctorProfileDao;
        this.doctorService = doctorService;

    }

    @Transactional
    @Override
    public DoctorProfile create(long doctorId, String bio, String description) {
        LOGGER.debug("Creating doctor profile for doctor with ID: {}", doctorId);
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        DoctorProfile profile = doctorProfileDao.create(doctor, bio, description);
        LOGGER.info("Doctor profile created for doctor with ID: {}", doctorId);
        return profile;
    }


    @Transactional(readOnly = true)
    @Override
    public DoctorProfile findByDoctorId(long id) {
        if(doctorProfileDao.getByDoctorId(id).isEmpty()) {
            throw new UserNotFoundException("Doctor profile not found for doctor ID: " + id);
        }
        return doctorProfileDao.getByDoctorId(id).get();
    }

    @Transactional
    @Override
    public void update(long doctorId, String bio, String description) {
        LOGGER.debug("Updating doctor profile for doctor with ID: {}", doctorId);
        DoctorProfile profile = findByDoctorId(doctorId);
        profile.setBio(bio);
        profile.setDescription(description);
        doctorProfileDao.update(profile);
        LOGGER.info("Doctor profile updated for doctor with ID: {}", doctorId);
    }
}
