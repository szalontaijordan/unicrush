package unicrush.model.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@code unicrush.model.db.DAOFactory}.
 *
 * @author Szalontai Jordán
 */
public class DAOFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOFactoryTest.class);

    public DAOFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class DAOFactory");
    }

    /**
     * Test of getInstance method, of class DAOFactory.
     */
    @Test
    public void testGetInstance() {
        DAOFactory factory1 = DAOFactory.getInstance();
        DAOFactory factory2 = DAOFactory.getInstance();

        Assert.assertTrue(factory1.equals(factory2));
    }

    /**
     * Test of createLevelDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateLevelDAO() {
        LevelDAO levelDao = DAOFactory.getInstance().createLevelDAO();
        Assert.assertTrue(levelDao instanceof LevelDAOSimple);
    }

    /**
     * Test of createUserDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateUserDAO() {
        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        Assert.assertTrue(userDao instanceof UserDAOSimple);
    }

    /**
     * Test of createScoreDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateScoreDAO() {
        ScoreDAO scoreDao = DAOFactory.getInstance().createScoreDAO();
        Assert.assertTrue(scoreDao instanceof ScoreDAOSimple);
    }
}
