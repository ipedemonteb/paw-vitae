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
import java.util.stream.Collectors;

@Service
public class DoctorOfficeServiceImpl implements DoctorOfficeService {

    private final DoctorOfficeDao doctorOfficeDao;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final NeighborhoodService neighborhoodService;
    private final SpecialtyService specialtyService;
    private final AppointmentService appointmentService;
    private static final int MAX_OFFICES = 7;

    @Autowired
    public DoctorOfficeServiceImpl(DoctorOfficeDao doctorOfficeDao, NeighborhoodService neighborhoodService, SpecialtyService specialtyService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService,@Lazy AppointmentService appointmentService) {
        this.doctorOfficeDao = doctorOfficeDao;
        this.neighborhoodService = neighborhoodService;
        this.specialtyService = specialtyService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.appointmentService = appointmentService;
    }

    @Transactional
    @Override
    public DoctorOffice create(Doctor doctor, DoctorOfficeForm form) {
        if (Boolean.TRUE.equals(form.getActive())) {
            throw new BussinesRuleException("exception.business.cannotCreateAndActivateOffice");
        }
        List<DoctorOffice> offices = this.getAllByDoctorId(doctor.getId());
        if (offices.stream().anyMatch(o -> o.getOfficeName().equalsIgnoreCase(form.getOfficeName()) && o.getNeighborhood().getId() == form.getNeighborhoodId())) {
            throw new BussinesRuleException("exception.business.duplicatedOffice");
        }
        validateMaxOffices(doctor.getId());
        Neighborhood nb = neighborhoodService.getById(form.getNeighborhoodId())
                .orElseThrow(NeighborhoodNotFoundException::new);
        List<Specialty> permitted = specialtyService.getByDoctorId(doctor.getId());
        List<Specialty> specs = form.getSpecialtyIds().stream()
                .map(sid -> specialtyService.getById(sid).orElseThrow(SpecialtyNotFoundException::new))
                .toList();
        specs.forEach(c -> {if (!permitted.contains(c)) {throw new SpecialtyNotOwnedException();}});
        DoctorOffice office = form.toEntity(doctor, nb, specs);
        return doctorOfficeDao.create(office);
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

        if (form.getActive() != null && !office.isActive() && form.getActive()) {
            validateMaxOffices(doctorId);
            validateActiveAvailability(form, doctorId,officeId);
            office.setActive(form.getActive());
        }
        if (form.getActive() != null && !form.getActive() && office.isActive()) {
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
            List<Specialty> permitted = specialtyService.getByDoctorId(doctorId);
            specs.forEach(c -> {if (!permitted.contains(c)) {throw new SpecialtyNotOwnedException();}});
            office.setSpecialties(specs);
        }
        return doctorOfficeDao.update(office);
    }



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

    @Transactional
    @Override
    public void updateSpecialties(Doctor doctor, List<Specialty> newSpecialties) {
        List<DoctorOffice> offices = doctorOfficeDao.getByDoctorId(doctor.getId());

        for (DoctorOffice office : offices) {
            List<Specialty> updatedList = office.getSpecialties().stream()
                    .filter(newSpecialties::contains)
                    .collect(Collectors.toList());

            if (updatedList.isEmpty()) {
                office.setActive(false);
            }

            office.setSpecialties(updatedList);
            doctorOfficeDao.update(office);
        }
    }
}
