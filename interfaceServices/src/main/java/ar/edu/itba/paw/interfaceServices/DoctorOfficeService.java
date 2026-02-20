package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface DoctorOfficeService {

    DoctorOffice create(Doctor doctor,  DoctorOfficeForm doctorOfficeForms);

    Optional<DoctorOffice> getById(long id);

    List<DoctorOffice> getByDoctorId(long doctorId, String status);

    List<DoctorOffice> getActiveByDoctorId(long doctorId);

    List<DoctorOffice> getAllByDoctorId(long doctorId);

    List<DoctorOffice> getAllByDoctorIdWithAvailability(long doctorId);

    DoctorOffice update(long officeId, DoctorOfficeForm form, long doctorId);

    void delete(long officeId, long doctorId);

    void updateSpecialties(Doctor doctor, List<Specialty> specialties);
}
