package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.CertificateForm;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;

import java.time.LocalDate;
import java.util.List;

public interface DoctorCertificationService {

     List<DoctorCertification> findByDoctorId(long id);

     void update(Doctor doctor, List<CertificateForm> certificates);
}
