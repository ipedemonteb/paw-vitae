package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return doctorOfficeDao.getActiveByDoctorId(doctorId);
    }

    @Transactional
    @Override
    public void update(List<DoctorOfficeForm> officeForms, Doctor doctor) {
        // 1) load *all* offices for this doctor (active + inactive if you ever want to re-activate)
        List<DoctorOffice> existing = doctorOfficeDao.getAllByDoctorId(doctor.getId());
        Map<Long,DoctorOffice> existingById =
                existing.stream().collect(Collectors.toMap(DoctorOffice::getId, Function.identity()));

        Set<Long> keepIds = new HashSet<>();

        // 2) create new or update existing (and set active=true)
        for (DoctorOfficeForm form : officeForms) {
            DoctorOffice match = existingById.values().stream().filter(d -> d.getNeighborhood().getId() == form.getNeighborhoodId() && d.getOfficeName().equalsIgnoreCase(form.getOfficeName().trim())).findFirst().orElse(null);
            if (match == null) {
                DoctorOffice created = new DoctorOffice();
                applyForm(created, form, doctor);
                created.setActive(true);
                doctorOfficeDao.create(created);
                keepIds.add(created.getId());
            } else {
                applyForm(match, form, doctor);
                match.setActive(true);
                doctorOfficeDao.update(match);
                keepIds.add(match.getId());
            }
        }

        // 3) any office *not* in keepIds → soft-delete
        for (DoctorOffice o : existing) {
            if (!keepIds.contains(o.getId()) && o.isActive()) {
                doctorOfficeDao.softDelete(o.getId());
            }
        }
    }

    private void applyForm(DoctorOffice office, DoctorOfficeForm form, Doctor doctor) {
        office.setDoctor(doctor);
        office.setOfficeName(form.getOfficeName());
        office.setNeighborhood(neighborhoodService.getById(form.getNeighborhoodId())
                .orElseThrow(() -> new IllegalArgumentException("Neighborhood not found")));
        List<Specialty> specs = form.getSpecialtyIds().stream()
                .map(id -> specialtyService.getById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Specialty "+id)))
                .collect(Collectors.toList());
        office.setSpecialties(specs);
    }

}
