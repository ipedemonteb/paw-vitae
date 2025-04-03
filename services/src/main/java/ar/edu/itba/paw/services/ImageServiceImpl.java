package ar.edu.itba.paw.services;
import ar.edu.itba.paw.interfacePersistence.ImageDao;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
@Service
public class ImageServiceImpl implements ImageService{
    private final ImageDao imageDao;
    @Autowired
    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Images create(long doctor_id, byte[] image) throws IOException {
       return imageDao.create(doctor_id, image);
    }

    @Override
    public Optional<Images> findById(long id) {
        return imageDao.findById(id);
    }

    @Override
    public Optional<Images> findByDoctorId(long doctor_id) {
        return imageDao.findByDoctorId(doctor_id);
    }
}
