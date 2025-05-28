package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorCertification;

import java.time.LocalDate;

public interface DoctorCertificationService {

    public DoctorCertification create(long doctorId, String certificateName, String issuingEntity, LocalDate issueDate);

    public DoctorCertification findByDoctorId(long id);
}
