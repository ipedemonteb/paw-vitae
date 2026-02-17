package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
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

    private static final Long DOC_ID = 1L;
    private static final Long IMG_ID = 1L;
    private static final String NAME = "Jane";
    private static final String LAST_NAME = "Smith";
    private static final String EMAIL = "jane@test.com";
    private static final String PASSWORD = "hashedpassword";
    private static final String PHONE = "987654321";
    private static final Double RATING = 4.5;
    private static final Integer RATING_COUNT = 10;
    private static final List<Locale> LANGUAGE = List.of(new Locale("es"));
    private static final Specialty SPECIALTY = new Specialty(1L, "Cardiology");
    private static final Coverage COVERAGE = new Coverage(1L, "Coverage A");

    private static final Doctor DOCTOR = new Doctor(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, "es",
             RATING, RATING_COUNT, true);

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
    @Mock
    private UserService userService;
    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    public void testCreateDoctorInvalidSpecialty() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(
                SpecialtyNotFoundException.class, () -> {
                    doctorService.create(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE,
                            List.of(1L), List.of(1L));
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
                    doctorService.create(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE,
                            List.of(1L), List.of(1L));
                }
        );
    }

    @Test
    public void testCreateDoctor() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(SPECIALTY));
        when(coverageService.findById(anyLong())).thenReturn(Optional.of(COVERAGE));
        when(doctorDao.create(
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(DOCTOR);

        //Exercise
        Doctor createdDoctor = doctorService.create(
                NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, LANGUAGE,
                List.of(SPECIALTY.getId()), List.of(COVERAGE.getId())
        );

        //Postconditions
        assertNotNull(createdDoctor);
        assertEquals(NAME, createdDoctor.getName());
        assertEquals(LAST_NAME, createdDoctor.getLastName());
        assertEquals(EMAIL, createdDoctor.getEmail());
    }

    @Test
    public void testSetImageInvalidUser() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                doctorService.setImage(DOC_ID, IMG_ID)
        );
    }

    @Test
    public void testSetImage() {
        //Preconditions
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                4.5, 10, true);
        when(doctorDao.getById(anyLong())).thenReturn(Optional.of(doctor));

        //Exercise
        doctorService.setImage(DOC_ID, IMG_ID);

        //Postconditions
        assertEquals(IMG_ID, doctor.getImageId());
    }

    @Test
    public void testUpdateDoctorRatingInvalidUser() {
        //Preconditions
        long rating = 5;
        when(doctorDao.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                doctorService.updateDoctorRating(DOC_ID, rating)
        );
    }

    @Test
    public void testUpdateDoctorRating() {
        //Preconditions
        long newRating = 5;
        double expectedRating = (RATING * RATING_COUNT + newRating) / (RATING_COUNT + 1);
        Doctor doctor = new Doctor(NAME, LAST_NAME, EMAIL, PASSWORD, PHONE, "es",
                RATING, RATING_COUNT, true);
        when(doctorDao.getById(anyLong())).thenReturn(Optional.of(doctor));

        //Exercise
        doctorService.updateDoctorRating(DOC_ID, newRating);

        //Postconditions
        assertEquals(expectedRating, doctor.getRating(), 0.01);
    }


}
