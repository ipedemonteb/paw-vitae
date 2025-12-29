package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.interfaceServices.DoctorExperienceService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        DoctorExperience experience = doctorExperienceDao.create(doctor, title, orgName, startDate, endDate);
        LOGGER.info("Doctor experience created for doctor with ID: {}", doctorId);
        return experience;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorExperience> findByDoctorId(long doctorId) {
        List<DoctorExperience> experiences = this.doctorExperienceDao.getByDoctorId(doctorId);
        if(experiences.isEmpty()) {
            throw new UserNotFoundException();
        }
        return experiences;
    }

    @Transactional
    @Override
    public void update(Doctor doctor, List<ExperienceForm> experiences) {
        List<DoctorExperience> existing = doctor.getExperiences();

        Map<Long, DoctorExperience> existingById =
                existing.stream().collect(Collectors.toMap(DoctorExperience::getId, Function.identity()));

        Set<Long> keepIds = new HashSet<>();

        if (experiences == null) {
            experiences = List.of();
        }

        for(ExperienceForm form : experiences) {
            DoctorExperience match = existingById.values().stream().filter(e ->
                    e.getPositionTitle().equals(form.getPositionTitle()) &&
                    e.getOrganizationName().equals(form.getOrganizationName()) &&
                    e.getStartDate().equals(form.getStartDate()) &&
                            (e.getEndDate() == null || e.getEndDate().equals(form.getEndDate()))
            ).findFirst().orElse(null);
            if(match == null) {
                DoctorExperience created = doctorExperienceDao.create(doctor, form.getPositionTitle(), form.getOrganizationName(), form.getStartDate(), form.getEndDate());
                keepIds.add(created.getId());
            } else {
                keepIds.add(match.getId());
            }
        }

        List<DoctorExperience> toDelete = existing.stream()
                .filter(experience -> !keepIds.contains(experience.getId()))
                .toList();

        for (DoctorExperience experience : toDelete) {
            doctorExperienceDao.delete(experience.getId());
            existing.remove(experience);
        }
    }
}
