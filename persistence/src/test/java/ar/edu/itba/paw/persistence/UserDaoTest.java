package ar.edu.itba.paw.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoTest {

    private static final String USER_TABLE = "users";

    private static final long PAT_ID = 1;

    private UserDaoImpl userDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userDao = new UserDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateUser() {
        //Preconditions
        String name = "John";
        String lastname = "Morgan";
        String email = "johnmorgan@test.com";
        String password = "hashedpassword";
        String phone = "123456789";
        String language = "es";

        //Exercise
        long id = userDao.create(name, lastname, email, password, phone, language).longValue();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = " + id +
                " AND name = '" + name + "'" +
                " AND last_name = '" + lastname + "'" +
                " AND email = '" + email + "'" +
                " AND password = '" + password + "'" +
                " AND phone = '" + phone + "'" +
                " AND language = '" + language + "'"
        ));
    }

    @Test
    public void testSetVerificationToken() {
        //Preconditions
        String token = "TESTTOKEN";

        //Exercise
        userDao.setVerificationToken(PAT_ID, token);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "verification_token = '" + token + "'"));
    }

    @Test
    public void testSetVerificationStatus() {
        //Preconditions
        boolean status = false;

        //Exercise
        userDao.setVerificationStatus(PAT_ID, status);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "is_verified = " + status));
    }

    @Test
    public void testSetResetPasswordToken() {
        //Preconditions
        String newResetToken = "NEWRESETTOKEN";

        //Exercise
        userDao.setResetPasswordToken(PAT_ID, newResetToken);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "reset_token = '" + newResetToken + "'"));
    }

    @Test
    public void testRemoveVerificationToken() {
        //Preconditions
        String verification_token = "VERIFTOKEN";

        //Exercise
        userDao.removeVerificationToken(verification_token);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND verification_token IS NULL"));
    }

    @Test
    public void testChangePassword() {
        //Preconditions
        String newPassword = "NEWPASSWORD";

        //Exercise
        userDao.changePassword(PAT_ID, newPassword);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND password = '" + newPassword + "'"));
    }

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
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND language = '" + newLanguage + "'"));
    }

    @Test
    public void testRemoveResetToken() {
        //Preconditions
        String reset_token = "RESTOKEN";

        //Exercise
        userDao.removeResetToken(reset_token);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND reset_token IS NULL"));
    }

    @Test
    public void testUpdateUser() {
        //Preconditions
        String newName = "Jane";
        String newLastName = "DeMorgan";
        String newPhone = "987654321";

        //Exercise
        userDao.update(PAT_ID, newName, newLastName, newPhone);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = " + PAT_ID +
                " AND name = '" + newName + "'" +
                " AND last_name = '" + newLastName + "'" +
                " AND phone = '" + newPhone + "'"
        ));
    }
}
