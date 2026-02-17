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
    public void testRemoveVerificationToken() {
        //Preconditions
        String verification_token = "VERIFTOKEN";

        //Exercise
        userDao.removeVerificationToken(verification_token);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = 1 AND verification_token IS NULL"));
    }


    @Test
    public void testTokenExpirationDateDoesNotExist() {
        //Preconditions
        String token = "NONEXISTINGTOKEN";

        //Exercise
        LocalDateTime expiration = userDao.tokenExpirationDate(token);

        //Postconditions
        assertNull(expiration);
    }

    @Test
    public void testTokenExpirationDateExists() {
        //Preconditions
        String token = "VERIFTOKEN";

        //Exercise
        LocalDateTime expiration = userDao.tokenExpirationDate(token);

        //Postconditions
        assertNotNull(expiration);
        assertEquals(expirationDate.getDayOfYear(), expiration.getDayOfYear());
        assertEquals(expirationDate.getYear(), expiration.getYear());
    }

}
