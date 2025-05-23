package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.Neighborhood;

import javax.print.Doc;
import java.util.List;

public interface DoctorOfficeService {
    List<DoctorOffice> create(List<DoctorOffice> doctorOffice);
    DoctorOffice create(DoctorOffice doctorOffice);
    List<DoctorOffice> transformToDoctorOffice(Doctor doctor, List<DoctorOfficeForm> officeForms);
}
