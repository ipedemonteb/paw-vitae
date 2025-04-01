package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(long id);

    User create(String name, String lastName, String email, String password, String phone);

}
