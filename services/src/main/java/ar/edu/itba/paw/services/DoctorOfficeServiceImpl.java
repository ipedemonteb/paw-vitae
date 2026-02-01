package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.*;
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
    private static final int MAX_OFFICES = 7;
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
    public DoctorOffice create(Doctor doctor, DoctorOfficeForm form) {
        if (Boolean.TRUE.equals(form.getActive())) {
            //TODO: CHECK
            throw new BussinesRuleException("exception.business.cannotCreateAndActivateOffice");
        }
        validateMaxOffices(doctor.getId());
        Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
                .orElseThrow(NeighborhoodNotFoundException::new);
        List<Specialty> specs = form.getSpecialtyIds().stream()
                .map(sid -> specialtyService.getById(sid).orElseThrow(SpecialtyNotFoundException::new))
                .toList();

        DoctorOffice office = form.toEntity(doctor, nb, specs);
        return doctorOfficeDao.create(office);
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
    public DoctorOffice transformToDoctorOffice(Doctor doctor, DoctorOfficeForm officeForm) {
            Neighborhood neighborhood = neighborhoodService.getById(officeForm.getNeighborhoodId()).orElseThrow(NeighborhoodNotFoundException::new); //TODO MAKE CUSTOM EXCEPTION AND MAKE EFFICIENT (THERE CAN BE REPEATED NEIGHBORHOODS)
            List<Specialty> specialties = specialtyService.getByIds(officeForm.getSpecialtyIds());
            DoctorOffice doctorOffice = officeForm.toEntity(doctor, neighborhood, specialties);
        return create(doctorOffice);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<DoctorOffice> getById(long id) {
        return doctorOfficeDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOffice> getByDoctorId(long doctorId, String status) {
        String statusLower = status.toLowerCase();
        return switch (statusLower) {
            case "active" -> this.doctorOfficeDao.getActiveByDoctorId(doctorId);
            case "inactive" -> this.doctorOfficeDao.getInactiveByDoctorId(doctorId);
            default -> this.doctorOfficeDao.getByDoctorId(doctorId);
        };
    }

    @Transactional
    @Override
    public List<DoctorOffice> getActiveByDoctorId(long doctorId) {
        return doctorOfficeDao.getActiveByDoctorId(doctorId);
    }

    @Transactional
    @Override
    public List<DoctorOffice> getInactiveByDoctorId(long doctorId) {
        return doctorOfficeDao.getInactiveByDoctorId(doctorId);
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
    public DoctorOffice update(long officeId, DoctorOfficeForm form, long doctorId) {
        DoctorOffice office = doctorOfficeDao.getById(officeId).orElseThrow(DoctorOfficeNotFoundException::new);

        if (office.getDoctor().getId() != doctorId) {
            throw new ResourceOwnershipException();
        }

        if (form.getActive() != null && !office.isActive() && Boolean.TRUE.equals(form.getActive())) {
            validateMaxOffices(doctorId);
        }

        if(form.getActive() != null && form.getActive() && !office.isActive()){
            validateActiveAvailability(form, doctorId,officeId);
            office.setActive(form.getActive());
        }
        if (form.getOfficeName() != null) {
            office.setOfficeName(form.getOfficeName());
        }
        if (form.getNeighborhoodId() != null ) {
            Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
                    .orElseThrow(NeighborhoodNotFoundException::new);
            office.setNeighborhood(nb);
        }
        if (form.getSpecialtyIds() != null && !form.getSpecialtyIds().isEmpty()) {
            List<Specialty> specs = form.getSpecialtyIds().stream()
                    .map(sid -> specialtyService.getById(sid).orElseThrow(SpecialtyNotFoundException::new))
                    .collect(Collectors.toList());
            office.setSpecialties(specs);
        }
        return doctorOfficeDao.update(office);
    }
//    @Transactional
//    @Override
//    public void update(DoctorOfficeForm form, Doctor inputDoctor) {
//        Doctor doctor = doctorService.getByIdWithAvailableOffices(inputDoctor.getId())
//                .orElseThrow(UserNotFoundException::new);
//        validateOfficeOwnership(form, doctor);
//        validateMaxOffices(form);
//        validateActiveAvailability(form, doctor.getId());
//
//        Map<Long, DoctorOffice> existingById = doctor.getDoctorOffices().stream()
//                .collect(Collectors.toMap(DoctorOffice::getId, Function.identity()));
//
//        List<DoctorOffice> newOffices = new ArrayList<>();
//
//        for (DoctorOfficeForm form : forms) {
//            Long id = form.getId();
//            if (id != null && existingById.containsKey(id)) {
//                DoctorOffice office = existingById.get(id);
//                boolean deleted = applyFormToEntity(office, form, doctor);
//                if (!deleted) {
//                    newOffices.add(office);
//                }
//            } else {
//                Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
//                        .orElseThrow(NeighborhoodNotFoundException::new);
//                List<Specialty> specs = form.getSpecialtyIds().stream()
//                        .map(sid -> specialtyService.getById(sid)
//                                .orElseThrow(SpecialtyNotFoundException::new))
//                        .toList();
//
//                DoctorOffice created = form.toEntity(doctor, nb, specs);
//                newOffices.add(created);
//            }
//        }
//
//        doctor.getDoctorOffices().clear();
//        doctor.getDoctorOffices().addAll(newOffices);
//    }


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
                office.setRemoved(LocalDateTime.now(ZoneId.systemDefault()));
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
                        .orElseThrow(SpecialtyNotFoundException::new))
                .toList();
        office.setSpecialties(specs);

        return false;
    }

    private void validateMaxOffices(long doctorId) {
        long activeCount = doctorOfficeDao.getByDoctorId(doctorId).size();
        if (activeCount >= MAX_OFFICES) {
            throw new BussinesRuleException("exception.business.tooManyOffices");
        }
    }

    private void validateActiveAvailability(DoctorOfficeForm form, long doctorId,long officeId) {
        List<DoctorOfficeAvailability> existingSlots = doctorOfficeAvailabilityService.getByDoctorId(doctorId);

        Set<Long> officesWithSlots = existingSlots.stream()
                .map(s -> s.getOffice().getId())
                .collect(Collectors.toSet());


            if (Boolean.TRUE.equals(form.getActive())) {
                if (!officesWithSlots.contains(officeId)) {
                    throw new BussinesRuleException("exception.business.officeMustHaveAvailabilityToBeActive");
                }
        }
    }

    @Transactional
    @Override
    public void delete(long officeId, long doctorId) {
        DoctorOffice office = doctorOfficeDao.getById(officeId).orElseThrow(DoctorOfficeNotFoundException::new);
        if (office.getDoctor().getId() != doctorId) {
            throw new ResourceOwnershipException();
        }

        if (appointmentService.officeHasAppointments(officeId)) {
            office.setRemoved(LocalDateTime.now(ZoneId.systemDefault()));
        } else {
            doctorOfficeDao.remove(officeId);
        }
    }
}
