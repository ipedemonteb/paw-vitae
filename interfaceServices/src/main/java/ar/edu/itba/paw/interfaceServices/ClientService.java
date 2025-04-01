package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;

import java.util.Optional;

public interface ClientService {

    Optional<Client> getById(long id);

    Client create(String name, String lastName, String email, String password, String phone, Coverage coverage);

}
