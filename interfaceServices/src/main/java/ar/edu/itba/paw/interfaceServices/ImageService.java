package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Images;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {
    Images create(long doctor_id, byte[] image) throws IOException;
    Optional<Images> findById(final long id);
    Optional<Images> findByDoctorId(final long doctor_id);
}
