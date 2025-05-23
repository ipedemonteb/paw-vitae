package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorOfficeServiceImpl implements DoctorOfficeService {

    private final DoctorOfficeDao doctorOfficeDao;
    private final NeighborhoodService neighborhoodService;

    @Autowired
    public DoctorOfficeServiceImpl(DoctorOfficeDao doctorOfficeDao, NeighborhoodService neighborhoodService) {
        this.doctorOfficeDao = doctorOfficeDao;
        this.neighborhoodService = neighborhoodService;
    }

    @Transactional
    @Override
    public DoctorOffice create(DoctorOffice doctorOffice) { // TODO LOG EVERYTHING, OPTIMIZE MAYBE?
        return doctorOfficeDao.create(doctorOffice);
    }

    @Transactional
    @Override
    public List<DoctorOffice> create(List<DoctorOffice> doctorOffice) {
        List<DoctorOffice> doctorOffices = new ArrayList<>();
        for (DoctorOffice office : doctorOffice) {
            doctorOffices.add(create(office));
        }
        return doctorOffices;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOffice> transformToDoctorOffice(Doctor doctor, List<DoctorOfficeForm> officeForms) {
        List<DoctorOffice> doctorOffices = new ArrayList<>();
        for (DoctorOfficeForm officeForm : officeForms) {
            Neighborhood neighborhood = neighborhoodService.getById(officeForm.getNeighborhoodId()).orElseThrow(() -> new IllegalArgumentException("Neighborhood not found")); //TODO MAKE CUSTOM EXCEPTION AND MAKE EFFICIENT (THERE CAN BE REPEATED NEIGHBORHOODS)
            DoctorOffice doctorOffice = officeForm.toEntity(doctor, neighborhood);
            doctorOffices.add(doctorOffice);
        }
        return doctorOffices;
    }
}
