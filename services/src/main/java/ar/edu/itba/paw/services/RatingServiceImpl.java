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
    public Rating create(double rating, long doctorId, long patientId, long appointmentId, String comment, long id) {
        Rating rating_aux = ratingDao.create(rating, doctorId, patientId, appointmentId, comment, id);
        doctorService.UpdateDoctorRating(doctorId, rating_aux.getRating());
        return rating_aux;
    }

    @Override
    public Optional<Rating> getRating(int id) {
        return ratingDao.getRating(id);
    }

    @Override
    public Optional<Rating> getRatingByAppointmentId(long appointmentId) {
        return ratingDao.getRatingByAppointmentId(appointmentId);
    }

    @Override
    public List<Rating> getRatingsByDoctorId(long doctorId) {
        return ratingDao.getRatingsByDoctorId(doctorId);
    }

    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return ratingDao.getRatingsByPatientId(patientId);
    }

}
