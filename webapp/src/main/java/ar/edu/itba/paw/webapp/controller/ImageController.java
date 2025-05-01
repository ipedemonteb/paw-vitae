package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
        Optional<Images> imageOpt = imageService.findById(id);
        byte[] imageBytes;
        if (imageOpt.isPresent()) {
            imageBytes = imageOpt.get().getImage();
        } else {
            ClassPathResource defaultImage = new ClassPathResource("/img/default_picture.png");
            try (InputStream inputStream = defaultImage.getInputStream()) {
                imageBytes = inputStream.readAllBytes();
            }
        }
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.getOutputStream().write(imageBytes);
        response.getOutputStream().flush();
    }
}