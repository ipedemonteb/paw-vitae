package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.models.exception.NeighborhoodNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

    private static final String NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john@test.com";
    private static final String PASSWORD = "hashedpassword";
    private static final String PHONE = "12345678";
    private static final List<Locale> LANGUAGE = List.of(new Locale("en"));
    private static final boolean VERIFIED = true;
    private static final long COVERAGE_ID = 1L;
    private static final long NEIGHBORHOOD_ID = 1L;
    private static final Coverage COVERAGE = new Coverage(COVERAGE_ID, "Coverage A");
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(NEIGHBORHOOD_ID, "Neighborhood A");
    private static final Patient PATIENT = new Patient(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE.toString(), COVERAGE, NEIGHBORHOOD, VERIFIED);

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;
    @Mock
    private CoverageService coverageService;
    @Mock
    private NeighborhoodService neighborhoodService;
    @Mock
    private PatientDao patientDao;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void testCreateInvalidCoverage() {
        //Preconditions
        when(coverageService.findById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(CoverageNotFoundException.class, () -> {
                patientService.create(NAME, LAST_NAME, EMAIL, PASSWORD,
                        PHONE, LANGUAGE, COVERAGE_ID, NEIGHBORHOOD_ID
                );
            }
        );
    }

    @Test
    public void testCreateInvalidNeighborhood() {
        //Preconditions
        when(coverageService.findById(anyLong())).thenReturn(Optional.of(COVERAGE));
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(NeighborhoodNotFoundException.class, () -> {
                patientService.create(NAME, LAST_NAME, EMAIL, PASSWORD,
                        PHONE, LANGUAGE, COVERAGE_ID, NEIGHBORHOOD_ID
                );
            }
        );
    }

    @Test
    public void testCreate() {
        //Preconditions
        when(coverageService.findById(anyLong())).thenReturn(Optional.of(COVERAGE));
        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
        when(patientDao.create(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(PATIENT);

        //Exercise
        Patient patient = patientService.create(NAME, LAST_NAME, EMAIL, PASSWORD,
                PHONE, LANGUAGE, COVERAGE_ID, NEIGHBORHOOD_ID);

        //Postconditions
        assertNotNull(patient);
        assertEquals(NAME, patient.getName());
        assertEquals(LAST_NAME, patient.getLastName());
        assertEquals(EMAIL, patient.getEmail());
    }

    @Test
    public void testUpdatePatientNoCoverage() {
        //Preconditions
        when(coverageService.findById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(CoverageNotFoundException.class, () -> {
                patientService.updatePatient(PATIENT, NAME, LAST_NAME, PHONE, COVERAGE_ID);
            }
        );
    }

    @Test
    public void testUpdatePatient() {
        //Preconditions
        Patient patient = new Patient(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE.toString(), COVERAGE, NEIGHBORHOOD, VERIFIED);
        String newName = "Fred";
        String newLastName = "Johnson";
        String newPhone = "87654321";
        long newCoverageId = 2L;
        String newCoverageName = "Coverage B";
        Coverage newCoverage = new Coverage(newCoverageId, newCoverageName);
        when(coverageService.findById(anyLong())).thenReturn(Optional.of(newCoverage));

        //Exercise
        patientService.updatePatient(patient, newName, newLastName, newPhone, COVERAGE_ID);

        //Postconditions
        assertEquals(newName, patient.getName());
        assertEquals(newLastName, patient.getLastName());
        assertEquals(newPhone, patient.getPhone());
        assertEquals(newCoverage.getName(), patient.getCoverage().getName());
    }
}
