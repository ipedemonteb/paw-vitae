package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClientDao {

    Optional<Client> getById(long id);

    Client create(String name, String lastName, String email, String password, String phone, Coverage coverage);

    Optional<Client> getByEmail(String email);

    void updateClient(long id, String name, String lastName, String phone, Coverage coverage);

    List<Client> getByIds(Set<Long> ids);

    void changePassword(long id, String password);
}
