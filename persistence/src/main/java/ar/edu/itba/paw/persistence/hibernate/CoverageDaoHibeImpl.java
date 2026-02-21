package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
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
    public List<Coverage> getAll() {
        return em.createQuery("FROM Coverage order by id", Coverage.class).getResultList();
    }

    @Override
    public List<Coverage> findByIds(List<Long> ids) {
        final TypedQuery<Coverage> query = em.createQuery(
                "FROM Coverage AS c WHERE c.id IN :ids",
                Coverage.class
        );
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public List<Coverage> findByDoctorId(long id) {
        final TypedQuery<Coverage> query = em.createQuery("SELECT c FROM Doctor d JOIN d.coverageList c WHERE d.id = :doctorId ORDER BY c.id ", Coverage.class);
        query.setParameter("doctorId", id);
        return query.getResultList();
    }
}
