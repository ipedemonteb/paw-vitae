package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;

import java.util.List;

public interface DoctorOfficeAvailabilityService {

    DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlotForm slot, DoctorOffice doctorOffice);

    List<DoctorOfficeAvailabilitySlot> create(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice);

    void update(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice);

    List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId);

}
