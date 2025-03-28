package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {

    Optional<Client> findById(final long id);

    Optional<Client> findByEmail(String email);

    Client create(long id, String email, String password, long coverageId, String coverage,String name, String phone, List<Appointment> appointments);

}
