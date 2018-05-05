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
public class ValidatorTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);
    
    public ValidatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Validator");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isEndGameSituation method, of class Validator.
     */
    @Test
    public void testIsEndGameSituation() {
        LOGGER.info("- Testing method isEndGameSituation");
    }

    /**
     * Test of isMaxScore method, of class Validator.
     */
    @Test
    public void testIsMaxScore() {
        LOGGER.info("- Testing method isMaxScore");
    }

    /**
     * Test of isNoMoreSteps method, of class Validator.
     */
    @Test
    public void testIsNoMoreSteps() {
        LOGGER.info("- Testing method isNoMoreSteps");
    }

    /**
     * Test of isTwoSelected method, of class Validator.
     */
    @Test
    public void testIsTwoSelected() {
        LOGGER.info("- Testing method isTwoSelected");
    }

    /**
     * Test of isMaxIterations method, of class Validator.
     */
    @Test
    public void testIsMaxIterations() {
        LOGGER.info("- Testing method isMaxIterations");
    }

    /**
     * Test of isNoIterations method, of class Validator.
     */
    @Test
    public void testIsNoIterations() {
        LOGGER.info("- Testing method isNoIterations");
    }

    /**
     * Test of isEmptyString method, of class Validator.
     */
    @Test
    public void testIsEmptyString() {
        LOGGER.info("- Testing method isEmptyString");
    }

    /**
     * Test of getChecks method, of class Validator.
     */
    @Test
    public void testGetChecks() {
        LOGGER.info("- Testing method getChecks");
    }
    
}
