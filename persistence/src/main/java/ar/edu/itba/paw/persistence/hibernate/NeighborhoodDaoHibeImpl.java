package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
import ar.edu.itba.paw.models.Neighborhood;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class NeighborhoodDaoHibeImpl implements NeighborhoodDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Neighborhood> getById(long id) {
        return Optional.ofNullable(em.find(Neighborhood.class, id));
    }

    @Override
    public List<Neighborhood> getAll() {
        return em.createQuery("FROM Neighborhood", Neighborhood.class)
                .getResultList();
    }
}
