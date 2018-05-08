package unicrush.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author szalontaijordan
 */
public class ValidatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);
    private static Validator validator;
    private static CandyCrushGame game;

    public ValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Validator");
        validator = Validator.getInstance();

        game = new CandyCrushGame();
        List<Level> levels = new ArrayList<>();
        levels.add(new Level.Builder(0, Level.Type.STANDARD, 5)
                .withCompleteScore(100).fillBoard().build());
        game.setLevels(levels);
        game.setCurrentLevelIndex(0);
        game.setPlayerScore(101);
    }

    /**
     * Test of isEndGameSituation method, of class Validator.
     */
    @Test
    public void testIsEndGameSituation() {
        LOGGER.info("- Testing method isEndGameSituation");

        Assert.assertTrue(validator.isEndGameSituation(game, 0));

        game.setPlayerScore(0);
        Assert.assertTrue(validator.isEndGameSituation(game, 0));
        Assert.assertFalse(validator.isEndGameSituation(game, 1));
    }

    /**
     * Test of isMaxScore method, of class Validator.
     */
    @Test
    public void testIsMaxScore() {
        LOGGER.info("- Testing method isMaxScore");

        game.setPlayerScore(101);
        Assert.assertTrue(validator.isMaxScore(game));

        game.setPlayerScore(0);
        Assert.assertFalse(validator.isMaxScore(game));
    }

    /**
     * Test of isNoMoreSteps method, of class Validator.
     */
    @Test
    public void testIsNoMoreSteps() {
        LOGGER.info("- Testing method isNoMoreSteps");

        Assert.assertTrue(validator.isNoMoreSteps(0));

        for (int i = 0; i < 100; i++) {
            Assert.assertFalse(validator.isNoMoreSteps((int) (Math.random() * 100) + 1));
        }
    }

    /**
     * Test of isTwoSelected method, of class Validator.
     */
    @Test
    public void testIsTwoSelected() {
        LOGGER.info("- Testing method isTwoSelected");

        Assert.assertTrue(validator.isTwoSelected("0,0;0,1"));
        Assert.assertFalse(validator.isTwoSelected("null;0,1"));
        Assert.assertFalse(validator.isTwoSelected("null;null"));
        Assert.assertFalse(validator.isTwoSelected("0,0;null"));
    }

    /**
     * Test of isMaxIterations method, of class Validator.
     */
    @Test
    public void testIsMaxIterations() {
        LOGGER.info("- Testing method isMaxIterations");

        Assert.assertTrue(validator.isMaxIterations(CandyCrushGame.MAX_ITERATION));

        for (int i = 0; i < 100; i++) {
            int rand = (int) (Math.random() * 100);
            if (rand != CandyCrushGame.MAX_ITERATION) {
                Assert.assertFalse(validator.isMaxIterations(rand));
            }
        }
    }

    /**
     * Test of isNoIterations method, of class Validator.
     */
    @Test
    public void testIsNoIterations() {
        LOGGER.info("- Testing method isNoIterations");

        Assert.assertTrue(validator.isNoIterations(0));

        for (int i = 0; i < 100; i++) {
            Assert.assertFalse(validator.isNoIterations((int) (Math.random() * 100) + 1));
        }
    }

    /**
     * Test of isEmptyString method, of class Validator.
     */
    @Test
    public void testIsEmptyString() {
        LOGGER.info("- Testing method isEmptyString");
        
        Assert.assertTrue(validator.isEmptyString(""));
        Assert.assertTrue(validator.isEmptyString(null));
        Assert.assertFalse(validator.isEmptyString("empty"));
        Assert.assertFalse(validator.isEmptyString("candy crush"));
        Assert.assertFalse(validator.isEmptyString("????"));
    }

}
