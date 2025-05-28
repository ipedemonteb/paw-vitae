package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.interfacePersistence.DoctorProfileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorCertificationServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorCertificationServiceImpl.class);
    private final DoctorCertificationDao doctorCertificationDao;

    @Autowired
    public DoctorCertificationServiceImpl( DoctorCertificationDao doctorCertificationDao) {
        this.doctorCertificationDao = doctorCertificationDao;
    }


}
