package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.ImageDao;
import ar.edu.itba.paw.models.Images;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ImageDaoHibeImpl implements ImageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Images create(byte[] image) {
        final Images images = new Images(image);
        em.persist(images);
        return images;
    }

    //@TODO: CHECK IMPLEMENTATION
    @Override
    public void deleteImage(long id) {
        Images images = em.find(Images.class, id);
        if (images != null) {
            em.remove(images);
        }
    }

    @Override
    public Optional<Images> findById(long id) {
        return Optional.ofNullable(em.find(Images.class, id));
    }
}
