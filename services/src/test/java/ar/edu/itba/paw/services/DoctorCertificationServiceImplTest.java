package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.CertificateForm; // Asumiendo que existe este DTO
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;
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
public class DoctorCertificationServiceImplTest {

    private static final long DOCTOR_ID = 1L;
    private static final String CERT_NAME = "Board Certified";
    private static final String ISSUER = "Medical Board";
    private static final LocalDate ISSUE_DATE = LocalDate.of(2020, 1, 1);

    private Doctor doctor;

    @Mock
    private DoctorCertificationDao doctorCertificationDao;

    @InjectMocks
    private DoctorCertificationServiceImpl doctorCertificationService;

    @Before
    public void setUp() {
        doctor = new Doctor("Jane", "Smith", "jane@test.com", "pass", "123", "es", 4.5, 10, true);
        doctor.setId(DOCTOR_ID);
        doctor.setCertifications(new ArrayList<>());
    }

    @Test
    public void testUpdateAddCertification() {
        // Preconditions

        CertificateForm form = new CertificateForm(CERT_NAME, ISSUER, ISSUE_DATE);
        List<CertificateForm> forms = List.of(form);

        DoctorCertification createdCert = new DoctorCertification(doctor, CERT_NAME, ISSUER, ISSUE_DATE);
        createdCert.setId(50L);

        when(doctorCertificationDao.create(eq(doctor), eq(CERT_NAME), eq(ISSUER), eq(ISSUE_DATE)))
                .thenReturn(createdCert);

        // Exercise
        doctorCertificationService.update(doctor, forms);

        // Postconditions
        verify(doctorCertificationDao, times(1))
                .create(eq(doctor), eq(CERT_NAME), eq(ISSUER), eq(ISSUE_DATE));
        verify(doctorCertificationDao, never()).delete(anyLong());
    }

    @Test
    public void testUpdateDeleteOrphanCertification() {
        // Preconditions
        DoctorCertification existingCert = new DoctorCertification(doctor, CERT_NAME, ISSUER, ISSUE_DATE);
        existingCert.setId(100L);
        doctor.getCertifications().add(existingCert);

        List<CertificateForm> forms = Collections.emptyList();

        // Exercise
        doctorCertificationService.update(doctor, forms);

        // Postconditions
        verify(doctorCertificationDao, times(1)).delete(100L);
        verify(doctorCertificationDao, never()).create(any(), anyString(), anyString(), any());

        assertEquals(0, doctor.getCertifications().size());
    }

    @Test
    public void testUpdateKeepExistingCertification() {
        // Preconditions
        DoctorCertification existingCert = new DoctorCertification(doctor, CERT_NAME, ISSUER, ISSUE_DATE);
        existingCert.setId(100L);
        doctor.getCertifications().add(existingCert);

        CertificateForm form = new CertificateForm(CERT_NAME, ISSUER, ISSUE_DATE);
        List<CertificateForm> forms = List.of(form);

        // Exercise
        doctorCertificationService.update(doctor, forms);

        // Postconditions
        verify(doctorCertificationDao, never()).create(any(), anyString(), anyString(), any());
        verify(doctorCertificationDao, never()).delete(anyLong());
    }

    @Test
    public void testFindByDoctorId() {
        // Preconditions
        List<DoctorCertification> certs = List.of(new DoctorCertification(doctor, CERT_NAME, ISSUER, ISSUE_DATE));
        when(doctorCertificationDao.getByDoctorId(DOCTOR_ID)).thenReturn(certs);

        // Exercise
        List<DoctorCertification> result = doctorCertificationService.findByDoctorId(DOCTOR_ID);

        // Postconditions
        assertEquals(1, result.size());
        assertEquals(CERT_NAME, result.get(0).getCertificateName());
    }
}