package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.interfaceServices.DoctorCertificationService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DoctorCertificationServiceImpl implements DoctorCertificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorCertificationServiceImpl.class);
    private final DoctorCertificationDao doctorCertificationDao;
    private final DoctorService doctorService;

    @Autowired
    public DoctorCertificationServiceImpl( DoctorCertificationDao doctorCertificationDao, DoctorService doctorService) {
        this.doctorCertificationDao = doctorCertificationDao;
        this.doctorService = doctorService;
    }

    @Transactional
    @Override
    public DoctorCertification create(long doctorId, String certificateName, String issuingEntity, LocalDate issueDate) {
        LOGGER.debug("Creating doctor profile for doctor with ID: {}", doctorId);
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        DoctorCertification certification = doctorCertificationDao.create(doctor, certificateName, issuingEntity, issueDate);
        LOGGER.info("Doctor profile created for doctor with ID: {}", doctorId);
        return certification;
    }


    @Transactional(readOnly = true)
    @Override
    public List<DoctorCertification> findByDoctorId(long id) {
        List<DoctorCertification> certifications = this.doctorCertificationDao.getByDoctorId(id);
        if(certifications.isEmpty()) {
            throw new UserNotFoundException("Doctor profile not found for doctor ID: " + id);
        }
        return certifications;
    }

    @Transactional
    @Override
    public void update(Doctor doctor, List<CertificateForm> certificates) {
        List<DoctorCertification> existing = doctor.getCertifications();

        Map<Long, DoctorCertification> existingById =
                existing.stream().collect(Collectors.toMap(DoctorCertification::getId, Function.identity()));

        Set<Long> keepIds = new HashSet<>();

        if (certificates == null) {
            certificates = Collections.emptyList();
        }

        for(CertificateForm form : certificates) {
            DoctorCertification match = existingById.values().stream().filter(c ->
                    c.getCertificateName().equals(form.getCertificateName()) &&
                    c.getIssuingEntity().equals(form.getIssuingEntity()) &&
                    c.getIssueDate().equals(form.getIssueDate())
            ).findFirst().orElse(null);
            if(match == null) {
                DoctorCertification created = doctorCertificationDao.create(doctor, form.getCertificateName(), form.getIssuingEntity(), form.getIssueDate());
                keepIds.add(created.getId());
            } else {
                keepIds.add(match.getId());
            }
        }

        List<DoctorCertification> toDelete = existing.stream()
                .filter(certification -> !keepIds.contains(certification.getId()))
                .toList();

        for (DoctorCertification certification : toDelete) {
            doctorCertificationDao.delete(certification.getId());
            existing.remove(certification);
        }
    }
}