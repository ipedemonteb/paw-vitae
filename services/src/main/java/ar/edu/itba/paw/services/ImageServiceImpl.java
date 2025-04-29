package ar.edu.itba.paw.services;
import ar.edu.itba.paw.interfacePersistence.ImageDao;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
@Service
public class ImageServiceImpl implements ImageService{
    Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageDao imageDao;
    @Autowired
    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Transactional
    @Override
    public Images create(long doctor_id, MultipartFile image) {
        if (image.isEmpty()) {
            return null;
        }
        Images toReturnImage = null;
        try {
            toReturnImage = imageDao.create(doctor_id, image.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error while creating image", e);
            return null;
        }
        LOGGER.debug("Image created: {}", toReturnImage);
        return toReturnImage;
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
