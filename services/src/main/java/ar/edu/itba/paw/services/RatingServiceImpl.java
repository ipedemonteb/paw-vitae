package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.ResourceOwnershipException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);
    private final RatingDao ratingDao;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MailService mailService;
    private final AppointmentService appointmentService;

    @Autowired
    public RatingServiceImpl(RatingDao ratingDao, DoctorService doctorService, PatientService patientService, MailService mailService, AppointmentService appointmentService) {
        this.ratingDao = ratingDao;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.mailService = mailService;
        this.appointmentService = appointmentService;
    }

    @Transactional
    @Override
    public Rating create(long rating,String email, long appointmentId, String comment) {
        Patient patient = patientService.getByEmail(email).orElseThrow(UserNotFoundException::new);
        Appointment appointment = appointmentService.getById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        if (patient.getId() != appointment.getPatient().getId()){
            throw new ResourceOwnershipException();
        }
        Doctor doctor = doctorService.getById(appointment.getDoctor().getId()).orElseThrow(UserNotFoundException::new);
        Rating rating_aux = ratingDao.create(rating, doctor, patient, appointment, comment);
        LOGGER.debug("Creating rating with rating: {}, doctorId: {}, patientId: {}, appointmentId: {}, comment: {}", rating, doctor.getId(), patient.getId(), appointmentId, comment);
        doctorService.updateDoctorRating(doctor.getId(), rating_aux.getRating());
        mailService.sendRatingMail(doctor, patient, appointment, rating_aux.getRating(), comment);
        LOGGER.info("Rating created with id: {} for doctor with id {} by patient with id {}", rating_aux.getId(), doctor.getId(), patient.getId());
        return rating_aux;

    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Rating> getRating(long id) {
        return ratingDao.getRating(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Rating> getRatingByAppointmentId(long appointmentId) {
        return ratingDao.getRatingByAppointmentId(appointmentId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Rating> getRatingsByDoctorId(long doctorId, int page, int pageSize) {
        return ratingDao.getRatingsByDoctorId(doctorId, page, pageSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return ratingDao.getRatingsByPatientId(patientId);
    }


    @Transactional(readOnly = true)
    @Override
    public Map<Rating, Patient> getFiveTopRatings() {
        return ratingDao.getFiveTopRatings().stream().collect(Collectors.toMap(
                rating -> rating,
                rating -> patientService.getById(rating.getPatient().getId()).orElseThrow(UserNotFoundException::new)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> getFiveTopRatingsByDoctorId(long doctorId) {
        return ratingDao.getFiveTopRatingsByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Rating> getAllRatings(int page, int pageSize,Long doctorId) {
        if (doctorId == null) return ratingDao.getAllRatings(page, pageSize);
        return ratingDao.getRatingsByDoctorId(doctorId,page,pageSize);
    }

}
