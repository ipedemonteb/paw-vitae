package ar.edu.itba.paw.webapp.utils;

import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {


    public static MultipartFile[] getFiles(FormDataMultiPart multiPart, String field) {
        if (multiPart == null) {
            return new MultipartFile[0];
        }
        List<FormDataBodyPart> parts = multiPart.getFields(field);
        if (parts == null || parts.isEmpty()) {
            return new MultipartFile[0];
        }
        List<MultipartFile> files = new ArrayList<>();
        for (FormDataBodyPart part : parts) {
            if (part == null) {
                continue;
            }
            byte[] content = readBytes(part);
            if (content.length == 0) {
                continue;
            }
            String filename = part.getContentDisposition() != null
                    ? part.getContentDisposition().getFileName()
                    : field;
            String mediaType = part.getMediaType() != null ? part.getMediaType().toString() : null;
            files.add(new SimpleMultipartFile(field, filename, mediaType, content));
        }
        return files.toArray(new MultipartFile[0]);
    }

    public static MultipartFile[] requireFiles(FormDataMultiPart multiPart, String field) {
        if (multiPart == null) {
            throw new BadRequestException("Missing multipart payload");
        }
        MultipartFile[] files = getFiles(multiPart, field);
        if (files.length == 0) {
            throw new BadRequestException("No files provided");
        }
        return files;
    }

    public static MultipartFile requireSingleFile(FormDataMultiPart multiPart, String field) {
        MultipartFile[] files = requireFiles(multiPart, field);
        if (files.length != 1) {
            throw new BadRequestException("Exactly one file must be provided");
        }
        return files[0];
    }

    private static byte[] readBytes(FormDataBodyPart part) {
        BodyPartEntity entity = (BodyPartEntity) part.getEntity();
        try (InputStream inputStream = entity.getInputStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            inputStream.transferTo(buffer);
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to read uploaded file", e);
        }
    }

    private static class SimpleMultipartFile implements MultipartFile {

        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        private SimpleMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content.clone();
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}
