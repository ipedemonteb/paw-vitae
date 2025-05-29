package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;

import java.time.LocalDate;
import java.util.List;

public interface DoctorCertificationDao {

        DoctorCertification create(Doctor doctor, String certificateName, String issuingEntity, LocalDate issueDat);

        List<DoctorCertification> getByDoctorId(long doctorId);

        void delete(long id);
}
