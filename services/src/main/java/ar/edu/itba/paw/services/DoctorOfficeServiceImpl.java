package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.NeighborhoodNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DoctorOfficeServiceImpl implements DoctorOfficeService {

    private final DoctorOfficeDao doctorOfficeDao;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final NeighborhoodService neighborhoodService;
    private final SpecialtyService specialtyService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Autowired
    public DoctorOfficeServiceImpl(DoctorOfficeDao doctorOfficeDao, NeighborhoodService neighborhoodService, SpecialtyService specialtyService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService, @Lazy DoctorService doctorService,@Lazy AppointmentService appointmentService) {
        this.doctorOfficeDao = doctorOfficeDao;
        this.neighborhoodService = neighborhoodService;
        this.specialtyService = specialtyService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
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
            Neighborhood neighborhood = neighborhoodService.getById(officeForm.getNeighborhoodId()).orElseThrow(() -> new NeighborhoodNotFoundException("Neighborhood not found")); //TODO MAKE CUSTOM EXCEPTION AND MAKE EFFICIENT (THERE CAN BE REPEATED NEIGHBORHOODS)
            List<Specialty> specialties = specialtyService.getByIds(officeForm.getSpecialtyIds());
            DoctorOffice doctorOffice = officeForm.toEntity(doctor, neighborhood, specialties);
            DoctorOffice created = create(doctorOffice);
            doctorOffice.setDoctorOfficeAvailability(doctorOfficeAvailabilityService.create(officeForm.getOfficeAvailabilitySlotForms(), created));
            doctorOffices.add(created);
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

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOffice> getAllByDoctorId(long doctorId) {
        return doctorOfficeDao.getByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOffice> getAllByDoctorIdWithAvailability(long doctorId) {
        return doctorOfficeDao.getByDoctorIdWithAvailability(doctorId);
    }

    @Transactional
    @Override
    public void update(
            List<DoctorOfficeForm> forms,
            Doctor inputDoctor
    ) {
        // 1) Load the managed Doctor + its offices
        Doctor doctor = doctorService.getByIdWithAvailableOffices(inputDoctor.getId()).orElseThrow(UserNotFoundException::new);

        // 2) Index existing offices by ID
        Map<Long, DoctorOffice> existingById = doctor.getDoctorOffices().stream()
                .collect(Collectors.toMap(DoctorOffice::getId, Function.identity()));

        // 3) Prepare the new list and keep-set
        List<DoctorOffice> newOffices = new ArrayList<>();

        for (DoctorOfficeForm form : forms) {
            Long id = form.getId();
            if (id != null && existingById.containsKey(id)) {
                // ── UPDATE existing ──
                DoctorOffice office = existingById.get(id);
                boolean deleted = applyFormToEntity(office, form, doctor);
                if (!deleted) {
                    newOffices.add(office);
                }


            } else {
                // ── CREATE new ──
                Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
                        .orElseThrow(NeighborhoodNotFoundException::new);
                List<Specialty> specs = form.getSpecialtyIds().stream()
                        .map(sid -> specialtyService.getById(sid)
                                .orElseThrow(SpecialtyNotFoundException::new))
                        .toList();

                DoctorOffice created = form.toEntity(doctor, nb, specs);
                newOffices.add(created);
            }
        }

        // 5) Replace the collection in one go
        doctor.getDoctorOffices().clear();
        doctor.getDoctorOffices().addAll(newOffices);

        // 6) On transaction commit, Hibernate will:
        //    • INSERT any new DoctorOffice (via cascade)
        //    • UPDATE any existing ones (times, active, removed flag)
        //    • NOT DELETE anything (orphanRemoval=false)
        //
        // No explicit dao.create/update/softDelete calls needed.
    }

    // reuse your applyFormToEntity or inline:

    private boolean applyFormToEntity(
            DoctorOffice office,
            DoctorOfficeForm form,
            Doctor doctor
    ) {
        office.setDoctor(doctor);
        office.setOfficeName(form.getOfficeName());
        office.setActive(form.getActive());

        if (form.getRemoved()) {
            if (appointmentService.officeHasAppointments(office.getId())) {
                office.setRemoved(LocalDateTime.now());
            } else {
                office.getSpecialties().clear();
                doctorOfficeDao.remove(office.getId());
                return true;
            }
        }

        Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
                .orElseThrow(NeighborhoodNotFoundException::new);
        office.setNeighborhood(nb);
        List<Specialty> specs = form.getSpecialtyIds().stream()
                .map(sid -> specialtyService.getById(sid)
                        .orElseThrow(() ->
                                new SpecialtyNotFoundException("Specialty "+sid+" not found")))
                .toList();
        office.setSpecialties(specs);

        return false;
    }

}
