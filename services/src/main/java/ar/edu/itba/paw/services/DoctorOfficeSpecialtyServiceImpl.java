package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeSpecialtyDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeSpecialtyService;
import ar.edu.itba.paw.models.DoctorOfficeSpecialty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorOfficeSpecialtyServiceImpl implements DoctorOfficeSpecialtyService {

    private final DoctorOfficeSpecialtyDao doctorOfficeSpecialtyDao;

    @Autowired
    public DoctorOfficeSpecialtyServiceImpl(DoctorOfficeSpecialtyDao doctorOfficeSpecialtyDao) {
        this.doctorOfficeSpecialtyDao = doctorOfficeSpecialtyDao;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorOfficeSpecialtyServiceImpl.class);

    @Override
    public List<DoctorOfficeSpecialty> getByOfficeId(long officeId) {
        LOGGER.debug("Retrieving specialties for office with id: {}", officeId);
        return this.doctorOfficeSpecialtyDao.getByOfficeId(officeId);
    }
}
