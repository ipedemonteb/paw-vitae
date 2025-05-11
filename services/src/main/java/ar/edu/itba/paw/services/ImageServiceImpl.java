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
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;
    Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Transactional
    @Override
    public Images create(MultipartFile image) {
        LOGGER.debug("Creating image with name '{}' and size {} bytes", image.getOriginalFilename(), image.getSize());
        if (image.isEmpty()) {
            LOGGER.warn("Image is empty");
            return null;
        }
        Images toReturnImage;
        try {
            toReturnImage = imageDao.create(image.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error while creating image", e);
            return null;
        }
        LOGGER.info("Image created with id: {}", toReturnImage.getId());
        return toReturnImage;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Images> findById(long id) {
        LOGGER.debug("Finding image with id {}", id);
        if (id == -1) {
            return Optional.empty();
        }
        return imageDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteImage(long id) {
        LOGGER.debug("Deleting image with id {}", id);
        imageDao.deleteImage(id);
        LOGGER.info("Image with id {} deleted", id); //If i reach this line then i can assume the image was deleted
    }

}
