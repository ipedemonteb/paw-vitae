package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Optional<Client> getById(long id);

    Client create(String name, String lastName, String email, String password, String phone, String language, String coverage);

    Optional<Client> getByEmail(String email);

     void updateClient(Client currentClient, String name, String lastName, String phone, Coverage coverage);

     Optional<Client> getClientWithAppointments(long id);
}
