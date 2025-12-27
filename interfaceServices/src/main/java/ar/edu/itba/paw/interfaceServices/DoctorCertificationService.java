package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.CertificateForm;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;

import java.time.LocalDate;
import java.util.List;

public interface DoctorCertificationService {

    public DoctorCertification create(long doctorId, String certificateName, String issuingEntity, LocalDate issueDate);

    public List<DoctorCertification> findByDoctorId(long id);

    public void update(Doctor doctor, List<CertificateForm> certificates);
}
