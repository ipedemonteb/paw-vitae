package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PatientDaoHibeImpl implements PatientDao {
    @Override
    public Optional<Patient> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Patient create(long id, String name, String lastName, String email, String password, String phone, String language, Coverage coverage) {
        return null;
    }

    @Override
    public Optional<Patient> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void updatePatient(long id, Long coverageId) {

    }

    @Override
    public List<Patient> getByIds(Set<Long> ids) {
        return List.of();
    }

    @Override
    public String getLanguage(long id) {
        return "";
    }

    @Override
    public Optional<Patient> getByVerificationToken(String token) {
        return Optional.empty();
    }

    @Override
    public void changeLanguage(long id, String language) {

    }

    @Override
    public Optional<Patient> getByResetToken(String token) {
        return Optional.empty();
    }

    @Override
    public int countAll() {
        return 0;
    }
}
