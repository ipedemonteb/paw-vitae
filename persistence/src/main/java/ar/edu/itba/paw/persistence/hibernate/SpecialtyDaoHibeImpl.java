package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.SpecialtyDao;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class SpecialtyDaoHibeImpl implements SpecialtyDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Specialty> getById(long id) {
        return Optional.ofNullable(em.find(Specialty.class, id));
    }

    @Override
    public Optional<Specialty> getByName(String name) {
        final TypedQuery<Specialty> query = em.createQuery(
                "FROM Specialty AS s WHERE s.key = :key",
                Specialty.class
        );
        query.setParameter("key", name);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Specialty> getAll() {
        return em.createQuery("FROM Specialty", Specialty.class).getResultList();
    }

    @Override
    public List<Specialty> getByIds(List<Long> ids) {
        final TypedQuery<Specialty> query = em.createQuery(
                "FROM Specialty AS s WHERE s.id IN :ids",
                Specialty.class
        );
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    //@TODO: IMPLEMENT AFTER IMPLEMENTING DOCTOR
    @Override
    public List<Specialty> getByDoctorId(long id) {
        return List.of();
    }
}
