package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.interfaceServices.DoctorExperienceService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.DoctorProfile;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DoctorExperienceServiceImpl implements DoctorExperienceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorExperienceServiceImpl.class);
    private final DoctorExperienceDao doctorExperienceDao;
    private final DoctorService doctorService;

    @Autowired
    public DoctorExperienceServiceImpl(DoctorExperienceDao doctorExperienceDao, DoctorService doctorService) {
        this.doctorExperienceDao = doctorExperienceDao;
        this.doctorService = doctorService;
    }

    @Transactional
    @Override
    public DoctorExperience create(long doctorId, String title, String orgName, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("Creating doctor experience for doctor with ID: {}", doctorId);
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(() -> new UserNotFoundException("Doctor not found with ID: " + doctorId));
        DoctorExperience experience = doctorExperienceDao.create(doctor, title, orgName, startDate, endDate);
        LOGGER.info("Doctor experience created for doctor with ID: {}", doctorId);
        return experience;
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorExperience findByDoctorId(long doctorId) {
        if(doctorExperienceDao.getByDoctorId(doctorId).isEmpty()) {
            throw new UserNotFoundException("Doctor profile not found for doctor ID: " + doctorId);
        }
        return doctorExperienceDao.getByDoctorId(doctorId).getFirst();
    }
}
