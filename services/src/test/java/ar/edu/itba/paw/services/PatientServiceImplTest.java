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
    private static final String LANGUAGE = "en";
    private static final boolean VERIFIED = true;
    private static final long COVERAGE_ID = 1L;
    private static final long NEIGHBORHOOD_ID = 1L;
    private static final Coverage COVERAGE = new Coverage(COVERAGE_ID, "Coverage A");
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(NEIGHBORHOOD_ID, "Neighborhood A");
    private static final Patient PATIENT = new Patient(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE, COVERAGE, NEIGHBORHOOD, VERIFIED);

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

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void testCreateInvalidCoverage() {
        //Preconditions
        when(coverageService.findById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(
                CoverageNotFoundException.class, () -> {
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
        assertThrows(
                NeighborhoodNotFoundException.class, () -> {
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
    public void testGetAllPatientsDisplayCountBelowTenThousand() {
        //Preconditions
        when(patientDao.countAll()).thenReturn(100);

        //Exercise
        String count = patientService.getAllPatientsDisplayCount();

        //Postconditions
        assertEquals("100", count);
    }

    @Test
    public void testGetAllPatientsDisplayCountAboveTenThousand() {
        //Preconditions
        when(patientDao.countAll()).thenReturn(100000);

        //Exercise
        String count = patientService.getAllPatientsDisplayCount();

        //Postconditions
        assertEquals("100k+", count);
    }
}
