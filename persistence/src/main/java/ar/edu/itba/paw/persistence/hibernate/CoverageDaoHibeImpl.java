package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class CoverageDaoHibeImpl implements CoverageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Coverage> findById(long id) {
        return Optional.ofNullable(em.find(Coverage.class, id));
    }

    @Override
    @Transactional
    public Coverage create(String name) {
        final Coverage coverage = new Coverage(name);
        em.persist(coverage);
        return coverage;
    }

    @Override
    public Optional<Coverage> findByName(String name) {
        final TypedQuery<Coverage> query = em.createQuery(
                "from Coverage as c where c.name = :name",
                Coverage.class
        );
        query.setParameter("name", name);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Coverage> getAll() {
        return em.createQuery("SELECT c FROM Coverage c", Coverage.class).getResultList();
    }

    @Override
    public List<Coverage> findByIds(List<Long> ids) {
        final TypedQuery<Coverage> query = em.createQuery(
                "from Coverage as c where c.id in :ids",
                Coverage.class
        );
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    // @TODO: Change query
//    @Override
//    public List<Coverage> findByDoctorId(long doctorId) {
//        return em.createQuery("SELECT c FROM Coverage c JOIN DoctorCoverage dc ON c.id = dc.coverage.id WHERE dc.doctor.id = :doctorId", Coverage.class)
//                .setParameter("doctorId", doctorId)
//                .getResultList();
//    }

    @Override
    public List<Coverage> findByDoctorId(long id) {
        return List.of();
    }
}
