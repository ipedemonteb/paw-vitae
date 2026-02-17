package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorOfficeServiceImplTest {

    private static final Long OFFICE_ID = 1L;
    private static final Long DOC_ID = 1L;
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(1L, "Neighborhood A");
    private static final Specialty SPECIALTY = new Specialty(1L, "Specialty A");
    private static final Specialty SPECIALTY_ALT = new Specialty(2L, "Specialty B");
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
             4.5, 10, true);
    private static final DoctorOffice DOCTOR_OFFICE = new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(SPECIALTY, SPECIALTY_ALT), "Office Name");
    private static final DoctorOfficeForm OFFICE_FORM = new DoctorOfficeForm(1L, "Office Name", List.of(1L, 2L));

    @Mock
    private SpecialtyService specialtyService;
    @Mock
    private DoctorOfficeDao doctorOfficeDao;

    @Mock
    private NeighborhoodService neighborhoodService;

    @InjectMocks
    private DoctorOfficeServiceImpl doctorOfficeService;

    @Test
    public void testCreateInactive() {
        //Preconditions
        DoctorOfficeForm validOfficeForm = new DoctorOfficeForm(1L, List.of(1L, 3L), "Office Name", true, false);

        //Exercise & Postconditions
        assertThrows(BussinesRuleException.class, () ->
                doctorOfficeService.create(DOCTOR, validOfficeForm)
        );
    }

    @Test
    public void testCreateInvalidNeighbourhood() {
        //Preconditions
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(NeighborhoodNotFoundException.class, () ->
                doctorOfficeService.create(DOCTOR, OFFICE_FORM)
        );
    }

    @Test
    public void testCreateInvalidSpecialty() {
        //Preconditions
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
        when(specialtyService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(SpecialtyNotFoundException.class, () ->
                doctorOfficeService.create(DOCTOR, OFFICE_FORM)
        );
    }

    @Test
    public void testCreate() {
        // Preconditions
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));

        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(SPECIALTY));

        when(specialtyService.getByDoctorId(anyLong())).thenReturn(List.of(SPECIALTY, SPECIALTY_ALT));

        when(doctorOfficeDao.create(any(DoctorOffice.class))).thenReturn(DOCTOR_OFFICE);

        DOCTOR.setId(DOC_ID);

        // Exercise
        DoctorOffice doctorOffice = doctorOfficeService.create(DOCTOR, OFFICE_FORM);

        // Postconditions
        assertNotNull(doctorOffice);
        assertEquals(DOCTOR.getId(), doctorOffice.getDoctor().getId());
        assertEquals(DOCTOR_OFFICE.getOfficeName(), doctorOffice.getOfficeName());
    }
    @Test
    public void testTransformToDoctorOfficeInvalidNeighborhood() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(NeighborhoodNotFoundException.class, () -> {
            doctorOfficeService.transformToDoctorOffice(DOCTOR, OFFICE_FORM);
        });
    }

    @Test
    public void testTransformToDoctorOffice() {
        //Preconditions
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
        DoctorOffice mockOffice = new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(SPECIALTY), "Office A");
        when(doctorOfficeDao.create(any(DoctorOffice.class))).thenReturn(mockOffice);

        //Exercise
        DoctorOffice doctorOffice = doctorOfficeService.transformToDoctorOffice(DOCTOR, OFFICE_FORM);

        //Postconditions
        assertNotNull(doctorOffice);
        assertEquals(DOCTOR.getId(), doctorOffice.getDoctor().getId());
    }

    @Test
    public void testUpdateInvalidDoctorOffice() {
        //Preconditions
        when(doctorOfficeDao.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(DoctorOfficeNotFoundException.class, () ->
                doctorOfficeService.update(OFFICE_ID, OFFICE_FORM, DOC_ID)
        );
    }

    @Test
    public void testUpdateNoOwnership() {
        //Preconditions
        when(doctorOfficeDao.getById(anyLong())).thenReturn(Optional.of(DOCTOR_OFFICE));

        //Exercise & Postconditions
        assertThrows(ResourceOwnershipException.class, () ->
                doctorOfficeService.update(OFFICE_ID, OFFICE_FORM, 999L)
        );
    }

    @Test
    public void testUpdateInvalidNeighbourhood() {
        //Preconditions
        DoctorOffice office = mock(DoctorOffice.class);
        Doctor doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(DOC_ID);
        when(office.getDoctor()).thenReturn(doctor);
        when(doctorOfficeDao.getById(anyLong())).thenReturn(Optional.of(office));
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(NeighborhoodNotFoundException.class, () ->
                doctorOfficeService.update(OFFICE_ID, OFFICE_FORM, DOC_ID)
        );
    }

    @Test
    public void testUpdateInvalidSpecialty() {
        //Preconditions
        DoctorOffice office = mock(DoctorOffice.class);
        Doctor doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(DOC_ID);
        when(office.getDoctor()).thenReturn(doctor);
        when(doctorOfficeDao.getById(anyLong())).thenReturn(Optional.of(office));
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
        when(specialtyService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(SpecialtyNotFoundException.class, () ->
                doctorOfficeService.update(OFFICE_ID, OFFICE_FORM, DOC_ID)
        );
    }

    @Test
    public void testUpdate() {
        // Preconditions
        DoctorOffice office = mock(DoctorOffice.class);
        Doctor doctor = mock(Doctor.class);

        when(doctor.getId()).thenReturn(DOC_ID);
        when(office.getDoctor()).thenReturn(doctor);
        when(office.getOfficeName()).thenReturn(OFFICE_FORM.getOfficeName());

        when(doctorOfficeDao.getById(anyLong())).thenReturn(Optional.of(office));
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(SPECIALTY));

        when(specialtyService.getByDoctorId(anyLong())).thenReturn(List.of(SPECIALTY, SPECIALTY_ALT));

        // Exercise
        doctorOfficeService.update(OFFICE_ID, OFFICE_FORM, DOC_ID);

        // Postconditions
        assertEquals(OFFICE_FORM.getOfficeName(), office.getOfficeName());
    }
}
