package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Images;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    Images create(MultipartFile image);

    Optional<Images> findById(long id);

    void deleteImage(long id);

}
