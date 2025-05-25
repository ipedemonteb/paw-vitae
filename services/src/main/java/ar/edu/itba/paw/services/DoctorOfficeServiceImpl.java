package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorOfficeServiceImpl implements DoctorOfficeService {

    private final DoctorOfficeDao doctorOfficeDao;
    private final NeighborhoodService neighborhoodService;
    private final SpecialtyService specialtyService;

    @Autowired
    public DoctorOfficeServiceImpl(DoctorOfficeDao doctorOfficeDao, NeighborhoodService neighborhoodService, SpecialtyService specialtyService) {
        this.doctorOfficeDao = doctorOfficeDao;
        this.neighborhoodService = neighborhoodService;
        this.specialtyService = specialtyService;
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

    @Transactional
    @Override
    public List<DoctorOffice> transformToDoctorOffice(Doctor doctor, List<DoctorOfficeForm> officeForms) {
        List<DoctorOffice> doctorOffices = new ArrayList<>();
        for (DoctorOfficeForm officeForm : officeForms) {
            Neighborhood neighborhood = neighborhoodService.getById(officeForm.getNeighborhoodId()).orElseThrow(() -> new IllegalArgumentException("Neighborhood not found")); //TODO MAKE CUSTOM EXCEPTION AND MAKE EFFICIENT (THERE CAN BE REPEATED NEIGHBORHOODS)
            List<Specialty> specialties = specialtyService.getByIds(officeForm.getSpecialtyIds());
            DoctorOffice doctorOffice = officeForm.toEntity(doctor, neighborhood, specialties);
            doctorOffices.add(doctorOffice);
        }
        return doctorOffices;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<DoctorOffice> getById(long id) {
        return doctorOfficeDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOffice> getByDoctorId(long doctorId) {
        return doctorOfficeDao.getByDoctorId(doctorId);
    }
}
