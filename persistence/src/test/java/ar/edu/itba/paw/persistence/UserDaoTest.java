package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.persistence.hibernate.UserDaoHibeImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoTest {

    private static final String USER_TABLE = "users";

    private static final long PAT_ID = 1;

    private static final LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

    @Autowired
    private UserDaoHibeImpl userDao;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Rollback
    @Test
    public void testSetVerificationToken() {
        //Preconditions
        String token = "TESTTOKEN";

        //Exercise
        userDao.setVerificationToken(PAT_ID, token, expirationDate);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "verification_token = '" + token + "'"));
    }

    @Rollback
    @Test
    public void testSetVerificationStatus() {
        //Preconditions
        boolean status = false;

        //Exercise
        userDao.setVerificationStatus(PAT_ID, status);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "is_verified = " + status));
    }

    @Rollback
    @Test
    public void testSetResetPasswordToken() {
        //Preconditions
        String newResetToken = "NEWRESETTOKEN";

        //Exercise
        userDao.setResetPasswordToken(PAT_ID, newResetToken, expirationDate);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "reset_token = '" + newResetToken + "'"));
    }

    @Rollback
    @Test
    public void testRemoveVerificationToken() {
        //Preconditions
        String verification_token = "VERIFTOKEN";

        //Exercise
        userDao.removeVerificationToken(verification_token);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND verification_token IS NULL"));
    }

    @Rollback
    @Test
    public void testChangePassword() {
        //Preconditions
        String newPassword = "NEWPASSWORD";

        //Exercise
        userDao.changePassword(PAT_ID, newPassword);
        em.flush();

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

    @Rollback
    @Test
    public void testChangeLanguage() {
        //Preconditions
        String newLanguage = "es";

        //Exercise
        userDao.changeLanguage(PAT_ID, newLanguage);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND language = '" + newLanguage + "'"));
    }

    @Rollback
    @Test
    public void testRemoveResetToken() {
        //Preconditions
        String reset_token = "RESTOKEN";

        //Exercise
        userDao.removeResetToken(reset_token);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND reset_token IS NULL"));
    }
}
