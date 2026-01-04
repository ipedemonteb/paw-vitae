package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

    private static final String NAME = "Jane";
    private static final String LAST_NAME = "Smith";
    private static final String EMAIL = "jane@test.com";
    private static final String PASSWORD = "hashedpassword";
    private static final String PHONE = "987654321";
    private static final List<Locale> LANGUAGE = List.of(new Locale("es"));
    private static final Specialty SPECIALTY = new Specialty(1L, "Cardiology");
    private static final Coverage COVERAGE = new Coverage(1L, "Coverage A");

    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            1L, 4.5, 10, true);
    private static final List<DoctorOfficeForm> OFFICES = List.of();

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DoctorDao doctorDao;
    @Mock
    private MailService mailService;
    @Mock
    private ImageService imageService;
    @Mock
    private CoverageService coverageService;
    @Mock
    private SpecialtyService specialtyService;
    @Mock
    private DoctorOfficeService doctorOfficeService;
    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    public void testCreateDoctorInvalidSpecialty() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(
                SpecialtyNotFoundException.class, () -> {
                    doctorService.create(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE, null,
                            List.of(1L), List.of(1L), OFFICES);
                }
        );
    }

    @Test
    public void testCreateDoctorInvalidCoverage() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(SPECIALTY));
        when(coverageService.findById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(
                CoverageNotFoundException.class, () -> {
                    doctorService.create(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE, null,
                            List.of(1L), List.of(1L), OFFICES);
                }
        );
    }

    @Test
    public void testCreateDoctor() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(SPECIALTY));
        when(coverageService.findById(anyLong())).thenReturn(Optional.of(COVERAGE));
        when(doctorDao.create(
                any(), any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(DOCTOR);

        //Exercise
        Doctor createdDoctor = doctorService.create(
                NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE, null,
                List.of(SPECIALTY.getId()), List.of(COVERAGE.getId()), OFFICES
        );

        //Postconditions
        assertNotNull(createdDoctor);
        assertEquals(NAME, createdDoctor.getName());
        assertEquals(LAST_NAME, createdDoctor.getLastName());
        assertEquals(EMAIL, createdDoctor.getEmail());
    }

    @Test
    public void testGetAllDoctorsDisplayCountBelowTenThousand() {
        //Preconditions
        when(doctorDao.countAll()).thenReturn(100);

        //Exercise
        String count = doctorService.getAllDoctorsDisplayCount();

        //Postconditions
        assertEquals("100", count);
    }

    @Test
    public void testGetAllDoctorsDisplayCountAboveTenThousand() {
        //Preconditions
        when(doctorDao.countAll()).thenReturn(100000);

        //Exercise
        String count = doctorService.getAllDoctorsDisplayCount();

        //Postconditions
        assertEquals("100k+", count);
    }

    @Test
    public void testSearchEmpty() {
        //Preconditions
        when(doctorDao.search(anyString(), anyInt())).thenReturn(List.of());

        //Exercise
        String search = doctorService.search("keyword", 2);

        //Postconditions
        assertNotNull(search);
        assertEquals("{\"doctors\": []}", search);
    }

    @Test
    public void testSearch() {
        //Preconditions
        when(doctorDao.search(anyString(), anyInt())).thenReturn(List.of(DOCTOR));
        String expectedResult = "{\"doctors\":[{\"id\":0,\"email\":\"jane@test.com\",\"name\":\"Jane\",\"lastName\":\"Smith\",\"phone\":\"987654321\",\"language\":\"es\",\"specialtyList\":[],\"coverageList\":[],\"doctorOffices\":[],\"rating\":4.5,\"ratingCount\":10,\"imageId\":1,\"experiences\":[],\"certifications\":[]}]}";

        //Exercise
        String search = doctorService.search("keyword", 2);

        //Postconditions
        assertNotNull(search);
        assertEquals(expectedResult, search);
    }
}
