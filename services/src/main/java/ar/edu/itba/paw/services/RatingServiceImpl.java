package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Rating;
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

    //@TODO: Fix
    @Transactional
    @Override
    public Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment) {
//        LOGGER.debug("Creating rating with rating: {}, doctorId: {}, patientId: {}, appointmentId: {}, comment: {}", rating, doctorId, patientId, appointmentId, comment);
//        Rating rating_aux = ratingDao.create(rating, doctorId, patientId, appointmentId, comment);
//        doctorService.UpdateDoctorRating(doctorId, rating_aux.getRating());
//        Doctor doctor = doctorService.getById(doctorId).orElseThrow(IllegalArgumentException::new);
//        Patient patient = patientService.getById(patientId).orElseThrow(IllegalArgumentException::new);
//        Appointment appointment = appointmentService.getById(appointmentId).orElseThrow(IllegalArgumentException::new);
//        mailService.sendRatingMail(doctor, patient, appointment, rating_aux.getRating(), comment);
//        LOGGER.info("Rating created with id: {} for doctor with id {} by patient with id {}", rating_aux.getId(), doctorId, patientId);
//        return rating_aux;
        return new Rating();
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
    public List<Rating> getRatingsByDoctorId(long doctorId) {
        return ratingDao.getRatingsByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return ratingDao.getRatingsByPatientId(patientId);
    }

    //@TODO: Fix
    @Transactional(readOnly = true)
    @Override
    public Map<Rating, Patient> getFiveTopRatings() {
//        return ratingDao.getFiveTopRatings().stream().collect(Collectors.toMap(
//                rating -> rating,
//                rating -> patientService.getById(rating.getPatientId()).orElseThrow(UserNotFoundException::new)));
        return Map.of();
    }
}
