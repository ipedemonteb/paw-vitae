package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingDao ratingDao;
    private final DoctorService doctorService;
    @Autowired
    public RatingServiceImpl(RatingDao ratingDao,
                             DoctorService doctorService) {
        this.ratingDao = ratingDao;
        this.doctorService = doctorService;
    }
    @Transactional
    @Override
    public Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment) {
        Rating rating_aux = ratingDao.create(rating, doctorId, patientId, appointmentId, comment);
        doctorService.UpdateDoctorRating(doctorId, rating_aux.getRating());
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
    public List<Rating> getRatingsByDoctorId(long doctorId) {
        return ratingDao.getRatingsByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return ratingDao.getRatingsByPatientId(patientId);
    }

}
