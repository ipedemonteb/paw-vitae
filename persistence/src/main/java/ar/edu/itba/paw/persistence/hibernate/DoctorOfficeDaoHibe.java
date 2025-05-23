package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.Neighborhood;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DoctorOfficeDaoHibe implements DoctorOfficeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOffice create(DoctorOffice doctorOffice) {
        em.persist(doctorOffice);
        return doctorOffice;
    }
}
