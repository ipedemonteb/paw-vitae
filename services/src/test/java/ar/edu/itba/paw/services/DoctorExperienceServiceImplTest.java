package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.ExperienceForm; // Asumo que este DTO existe
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DoctorExperienceServiceImplTest {

    private static final long DOCTOR_ID = 1L;
    private static final String TITLE = "Surgeon";
    private static final String ORG = "Hospital A";
    private static final LocalDate START = LocalDate.of(2020, 1, 1);
    private static final LocalDate END = LocalDate.of(2022, 1, 1);

    private Doctor doctor;

    @Mock
    private DoctorExperienceDao doctorExperienceDao;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorExperienceServiceImpl doctorExperienceService;

    @Before
    public void setUp() {
        doctor = new Doctor("Jane", "Smith", "jane@test.com", "pass", "123", "es", 4.5, 10, true);
        doctor.setId(DOCTOR_ID);
        doctor.setExperiences(new ArrayList<>());
    }

    @Test
    public void testUpdateAddOneNewExperience() {
        // Preconditions

        ExperienceForm form = new ExperienceForm(TITLE, ORG, START, END);
        List<ExperienceForm> forms = List.of(form);

        DoctorExperience createdExp = new DoctorExperience(doctor,TITLE, ORG, START, END);
        createdExp.setId(100L);
        when(doctorExperienceDao.create(eq(doctor), eq(TITLE), eq(ORG), eq(START), eq(END)))
                .thenReturn(createdExp);

        // Exercise
        doctorExperienceService.update(doctor, forms);

        // Postconditions
        verify(doctorExperienceDao, times(1))
                .create(eq(doctor), eq(TITLE), eq(ORG), eq(START), eq(END));
        verify(doctorExperienceDao, never()).delete(anyLong());
    }

    @Test
    public void testUpdateDeleteOrphanExperience() {
        // Preconditions
        DoctorExperience existingExp = new DoctorExperience(doctor,TITLE, ORG, START, END);
        existingExp.setId(100L);
        doctor.getExperiences().add(existingExp);


        List<ExperienceForm> forms = Collections.emptyList();

        // Exercise
        doctorExperienceService.update(doctor, forms);

        // Postconditions
        verify(doctorExperienceDao, never()).create(any(), anyString(), anyString(), any(), any());

        verify(doctorExperienceDao, times(1)).delete(100L);

        assertEquals(0, doctor.getExperiences().size());
    }

    @Test
    public void testUpdateKeepExistingExperience() {
        // Preconditions

        DoctorExperience existingExp = new DoctorExperience(doctor,TITLE, ORG, START, END);
        existingExp.setId(100L);
        doctor.getExperiences().add(existingExp);

        ExperienceForm form = new ExperienceForm(TITLE, ORG, START, END);
        List<ExperienceForm> forms = List.of(form);

        // Exercise
        doctorExperienceService.update(doctor, forms);

        // Postconditions
        verify(doctorExperienceDao, never()).create(any(), anyString(), anyString(), any(), any());
        verify(doctorExperienceDao, never()).delete(anyLong());
    }

    @Test
    public void testUpdateModifyExperience() {
        // Preconditions
        DoctorExperience existingExp = new DoctorExperience(doctor,TITLE, ORG, START, END );
        existingExp.setId(100L);
        doctor.getExperiences().add(existingExp);

        String newTitle = "Senior Surgeon";
        ExperienceForm form = new ExperienceForm(newTitle, ORG, START, END);
        List<ExperienceForm> forms = List.of(form);

        DoctorExperience newExp = new DoctorExperience(doctor, newTitle,ORG,START, END);
        newExp.setId(101L);
        when(doctorExperienceDao.create(eq(doctor), eq(newTitle), eq(ORG), eq(START), eq(END)))
                .thenReturn(newExp);

        // Exercise
        doctorExperienceService.update(doctor, forms);

        // Postconditions
        verify(doctorExperienceDao, times(1))
                .create(eq(doctor), eq(newTitle), eq(ORG), eq(START), eq(END));

        verify(doctorExperienceDao, times(1)).delete(100L);
    }

    @Test
    public void testFindByDoctorId() {
        // Preconditions
        List<DoctorExperience> experiences = List.of(new DoctorExperience(doctor,TITLE, ORG, START, END));
        when(doctorExperienceDao.getByDoctorId(DOCTOR_ID)).thenReturn(experiences);

        // Exercise
        List<DoctorExperience> result = doctorExperienceService.findByDoctorId(DOCTOR_ID);

        // Postconditions
        assertEquals(1, result.size());
        assertEquals(TITLE, result.get(0).getPositionTitle());
        verify(doctorExperienceDao).getByDoctorId(DOCTOR_ID);
    }
}