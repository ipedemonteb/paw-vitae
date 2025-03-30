package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Client;

import java.util.Optional;

public interface ClientService {

    Client create(String email, String password, long coverageId, String coverage,String name, String phone);

    Optional<Client> findById(long id);

    Optional<Client> findByEmail(String email);

}
