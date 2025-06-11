package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;

import java.util.List;
import java.util.Optional;

public interface DoctorOfficeService {
    List<DoctorOffice> create(List<DoctorOffice> doctorOffice);
    DoctorOffice create(DoctorOffice doctorOffice);
    List<DoctorOffice> transformToDoctorOffice(Doctor doctor, List<DoctorOfficeForm> officeForms);
    Optional<DoctorOffice> getById(long id);
    List<DoctorOffice> getByDoctorId(long doctorId);
    List<DoctorOffice> getAllByDoctorId(long doctorId);
    void update(List<DoctorOfficeForm> officeForms,Doctor doctor);
}
