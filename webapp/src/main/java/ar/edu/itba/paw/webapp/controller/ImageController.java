package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/doctor/{doctorId}/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void getDoctorImage(@PathVariable("doctorId") long doctorId, HttpServletResponse response) throws IOException {
        Optional<Images> imageOpt = imageService.findByDoctorId(doctorId);

        if (imageOpt.isPresent()) {
            byte[] imageBytes = imageOpt.get().getImage();
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(imageBytes);
        } else {
            // You could serve a default image here if you want
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        response.getOutputStream().flush();
    }
}