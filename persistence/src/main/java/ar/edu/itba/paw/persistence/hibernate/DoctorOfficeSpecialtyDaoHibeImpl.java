package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeSpecialtyDao;
import ar.edu.itba.paw.models.DoctorOfficeSpecialty;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DoctorOfficeSpecialtyDaoHibeImpl implements DoctorOfficeSpecialtyDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DoctorOfficeSpecialty> getByOfficeId(long officeId) {
        return em.createQuery(
                "FROM DoctorOfficeSpecialty d WHERE d.id.officeId = :officeId",
                        DoctorOfficeSpecialty.class
        )
                .setParameter("officeId", officeId)
                .getResultList();
    }
}
