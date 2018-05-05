package unicrush.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author szalontaijordan
 */
public class LevelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelTest.class);
        
    public LevelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Level");
    }

    /**
     * Test of getBoardState method, of class Level.
     */
    @Test
    public void testGetBoardState() {
        LOGGER.info("- Testing method getBoardState");
    }

    /**
     * Test of toString method, of class Level.
     */
    @Test
    public void testToString() {
        LOGGER.info("- Testing method toString");
    }

    /**
     * Test of createCoordinates method, of class Level.
     */
    @Test
    public void testCreateCoordinates() {
        LOGGER.info("- Testing method createCoordinates");
    }
}
