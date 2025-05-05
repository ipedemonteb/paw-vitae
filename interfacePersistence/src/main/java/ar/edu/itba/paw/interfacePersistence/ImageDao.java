package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Images;

import java.util.Optional;

public interface ImageDao {

    Images create(byte[] image);

    Optional<Images> findById(final long id);

}
