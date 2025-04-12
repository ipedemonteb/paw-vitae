package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {

    Optional<? extends User> getByEmail(String email);


}
