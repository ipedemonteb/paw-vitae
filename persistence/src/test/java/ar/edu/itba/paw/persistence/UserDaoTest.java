package ar.edu.itba.paw.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoTest {

    private static final String SQL_QUERY_VERIFICATIONTOKEN = "SELECT verification_token FROM users WHERE id = ?";
    private static final String SQL_QUERY_ISVERIFIED = "SELECT is_verified FROM users WHERE id = ?";
    private static final String SQL_QUERY_RESETTOKEN = "SELECT reset_token FROM users WHERE id = ?";
    private static final String SQL_QUERY_PASSWORD = "SELECT password FROM users WHERE id = ?";
    private static final String SQL_QUERY_LANGUAGE = "SELECT language FROM users WHERE id = ?";

    private static final long PAT_ID = 1;

    private UserDaoImpl userDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userDao = new UserDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testSetVerificationToken() {
        //Preconditions
        String token = "TESTTOKEN";

        //Exercise
        userDao.setVerificationToken(PAT_ID, token);

        //Postconditions
        List<String> finalToken = jdbcTemplate.query(SQL_QUERY_VERIFICATIONTOKEN,
                new Object[]{PAT_ID},
                (rs, rowNum) -> rs.getString("verification_token")
        );
        assertFalse(finalToken.isEmpty());
        assertEquals(1, finalToken.size());
        assertEquals(token, finalToken.getFirst());
    }

    @Test
    public void testSetVerificationStatus() {
        //Preconditions
        boolean status = false;

        //Exercise
        userDao.setVerificationStatus(PAT_ID, status);

        //Postconditions
        List<Boolean> finalStatus = jdbcTemplate.query(SQL_QUERY_ISVERIFIED,
                new Object[]{PAT_ID},
                (rs, rowNum) -> rs.getBoolean("is_verified")
        );
        assertFalse(finalStatus.isEmpty());
        assertEquals(1, finalStatus.size());
        assertEquals(status, finalStatus.getFirst());
    }

    @Test
    public void testSetResetPasswordToken() {
        //Preconditions
        String newResetToken = "NEWRESETTOKEN";

        //Exercise
        userDao.setResetPasswordToken(PAT_ID, newResetToken);

        //Postconditions
        List<String> finalResetToken = jdbcTemplate.query(SQL_QUERY_RESETTOKEN,
                new Object[]{PAT_ID},
                (rs, rowNum) -> rs.getString("reset_token")
        );
        assertFalse(finalResetToken.isEmpty());
        assertEquals(1, finalResetToken.size());
        assertEquals(newResetToken, finalResetToken.getFirst());
    }

    //@TODO: CHECK NULL IF ITS OK
    @Test
    public void testRemoveVerificationToken() {
        //Preconditions
        String verification_token = "VERIFICATIONTOKEN";
        long id = jdbcInsertUser.executeAndReturnKey(Map.of(
                "name", "John",
                "last_name", "Doe",
                "email", "johndoe@example.com",
                "password", "hashedpassword",
                "phone", "123456789",
                "language", "es",
                "verification_token", verification_token
        )).longValue();

        //Exercise
        userDao.removeVerificationToken(verification_token);

        //Postconditions
        List<String> finalToken = jdbcTemplate.query(SQL_QUERY_VERIFICATIONTOKEN,
                new Object[]{id},
                (rs, rowNum) -> rs.getString("verification_token")
        );
        assertNull(finalToken.getFirst());
    }

    @Test
    public void testChangePassword() {
        //Preconditions
        String newPassword = "NEWPASSWORD";

        //Exercise
        userDao.changePassword(PAT_ID, newPassword);

        //Postconditions
        List<String> finalPassword = jdbcTemplate.query(SQL_QUERY_PASSWORD,
                new Object[]{PAT_ID},
                (rs, rowNum) -> rs.getString("password")
        );
        assertFalse(finalPassword.isEmpty());
        assertEquals(1, finalPassword.size());
        assertEquals(newPassword, finalPassword.getFirst());
    }

    //@TODO: CHECK IF ID DOES NOT EXIST
    @Test
    public void testGetLanguageById() {
        //Preconditions

        //Exercise
        String language = userDao.getLanguageById(PAT_ID);

        //Postconditions
        assertEquals("en", language);
    }

    @Test
    public void testChangeLanguage() {
        //Preconditions
        String newLanguage = "es";

        //Exercise
        userDao.changeLanguage(PAT_ID, newLanguage);

        //Postconditions
        List<String> finalLanguage = jdbcTemplate.query(SQL_QUERY_LANGUAGE,
                new Object[]{PAT_ID},
                (rs, rowNum) -> rs.getString("language")
        );
        assertFalse(finalLanguage.isEmpty());
        assertEquals(1, finalLanguage.size());
        assertEquals(newLanguage, finalLanguage.getFirst());
    }

    @Test
    public void testRemoveResetToken() {
        //Preconditions
        String reset_token = "RESETTOKEN";
        long id = jdbcInsertUser.executeAndReturnKey(Map.of(
                "name", "John",
                "last_name", "Doe",
                "email", "johndoe@example.com",
                "password", "hashedpassword",
                "phone", "123456789",
                "language", "es",
                "reset_token", reset_token
        )).longValue();

        //Exercise
        userDao.removeResetToken(reset_token);

        //Postconditions
        List<String> finalToken = jdbcTemplate.query(SQL_QUERY_RESETTOKEN,
                new Object[]{id},
                (rs, rowNum) -> rs.getString("reset_token")
        );
        assertNull(finalToken.getFirst());
    }
}
