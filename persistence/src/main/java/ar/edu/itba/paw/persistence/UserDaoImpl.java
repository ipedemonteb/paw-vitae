//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfacePersistence.UserDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Repository
//public class UserDaoImpl implements UserDao {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final SimpleJdbcInsert jdbcInsert;
//
//
//
//    @Autowired
//    public UserDaoImpl(final DataSource dataSource) {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("users")
//                .usingGeneratedKeyColumns("id");
//    }
//
//    @Override
//    public Number create(String name, String lastName, String email, String password, String phone, String language) {
//        final Map<String, Object> args = new HashMap<>();
//        args.put("name", name);
//        args.put("last_name", lastName);
//        args.put("email", email);
//        args.put("password", password);
//        args.put("phone", phone);
//        args.put("language", language);
//        args.put("is_verified", false);
//        return jdbcInsert.executeAndReturnKey(args);
//    }
//
//    @Override
//    public void setVerificationToken(long id, String token,LocalDateTime expirationDate) {
//        jdbcTemplate.update("UPDATE users SET verification_token = ?, token_expiration_date = ? WHERE id = ?", token, expirationDate, id);
//
//    }
//
//    @Override
//    public void setVerificationStatus(long id, boolean status) {
//        jdbcTemplate.update("UPDATE users SET is_verified = ? WHERE id = ?", status, id);
//    }
//
//    @Override
//    public void setResetPasswordToken(long id, String token,LocalDateTime expirationDate) {
//        jdbcTemplate.update("UPDATE users SET reset_token = ?, token_expiration_date = ? WHERE id = ?", token, expirationDate, id);
//    }
//
//    @Override
//    public void removeVerificationToken(String token) {
//        jdbcTemplate.update("UPDATE users SET verification_token = NULL WHERE verification_token = ?", token);
//    }
//
//    @Override
//    public void changePassword(long id, String password) {
//        jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", password, id);
//    }
//
//    @Override
//    public String getLanguageById(long id) {
//        return jdbcTemplate.queryForObject("SELECT language FROM users WHERE id = ?", String.class, id);
//    }
//
//    @Override
//    public void changeLanguage(long id, String language) {
//        jdbcTemplate.update("UPDATE users SET language = ? WHERE id = ?", language, id);
//    }
//
//    @Override
//    public void removeResetToken(String token) {
//        jdbcTemplate.update("UPDATE users SET reset_token = NULL WHERE reset_token = ?", token);
//    }
//    @Override
//    public LocalDateTime tokenExpirationDate(String token) {
//        return jdbcTemplate.queryForObject("SELECT token_expiration_date FROM users WHERE reset_token = ? OR verification_token = ? ", LocalDateTime.class,token,token);
//    }
//    @Override
//    public void update(long id, String name, String lastName, String phone) {
//        jdbcTemplate.update("UPDATE users SET name = ?, last_name = ?, phone = ? WHERE id = ?", name, lastName, phone, id);
//    }
//}
