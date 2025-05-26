package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeId;
import ar.edu.itba.paw.models.Neighborhood;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class DoctorOfficeDaoHibe implements DoctorOfficeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOffice create(DoctorOffice doctorOffice) {
        doctorOffice.setNeighborhood(em.getReference(Neighborhood.class, doctorOffice.getNeighborhood().getId()));
        em.persist(doctorOffice);
        return doctorOffice;
    }

    @Override
    public Optional<DoctorOffice> getById(long id) {
        return Optional.ofNullable(em.find(DoctorOffice.class, id));
    }

    @Override
    public List<DoctorOffice> getByDoctorId(long doctorId) {
        return em.createQuery("FROM DoctorOffice d WHERE d.doctor.id = :doctorId AND d.active = TRUE ", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public void update( long doctorId,List<DoctorOffice> disabledOffices, List<DoctorOffice> officesToCreate, List<DoctorOffice> officesToActivate) {
        for (DoctorOffice current : disabledOffices) {
                em.merge(current);
        }

        for (DoctorOffice newOffice : officesToCreate) {
            String normalizedName = newOffice.getOfficeName().trim().toLowerCase();
            Long neighborhoodId = newOffice.getNeighborhood().getId();

            Doctor doctorRef = em.getReference(Doctor.class, doctorId);
            Neighborhood neighborhoodRef = em.getReference(Neighborhood.class, neighborhoodId);
            newOffice.setDoctor(doctorRef);
            newOffice.setNeighborhood(neighborhoodRef);
            newOffice.setActive(true);
            em.persist(newOffice);
        }
        for (DoctorOffice officeToActivate : officesToActivate) {
            officeToActivate.setActive(true);
            em.merge(officeToActivate);
        }
    }
    @Override
    public List<DoctorOffice> getByNameAndNeighborhoodId(String officeName, long neighborhoodId, long doctorId){
        return em.createQuery(
                        "SELECT d FROM DoctorOffice d " +
                                "WHERE d.doctor.id = :doctorId " +
                                "AND d.neighborhood.id = :neighborhoodId " +
                                "AND LOWER(TRIM(d.officeName)) = :name", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .setParameter("neighborhoodId", neighborhoodId)
                .setParameter("name", officeName.trim().toLowerCase())
                .getResultList();
    }

}
