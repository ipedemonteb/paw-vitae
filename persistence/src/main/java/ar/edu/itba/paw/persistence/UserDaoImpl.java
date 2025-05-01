package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void setVerificationToken(long id, String token) {
        jdbcTemplate.update("UPDATE users SET verification_token = ? WHERE id = ?", token, id);
    }

    @Override
    public void setVerificationStatus(long id, boolean status) {
        jdbcTemplate.update("UPDATE users SET is_verified = ? WHERE id = ?", status, id);
    }

    @Override
    public void setResetPasswordToken(long id, String token) {
        jdbcTemplate.update("UPDATE users SET reset_token = ? WHERE id = ?", token, id);
    }

    @Override
    public void removeVerificationToken(String token) {
        jdbcTemplate.update("UPDATE users SET verification_token = NULL WHERE verification_token = ?", token);
    }


}
