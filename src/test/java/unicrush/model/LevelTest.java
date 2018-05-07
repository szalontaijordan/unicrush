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
    private static Level level;

    public LevelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Level");

        level = new Level.Builder(Level.Type.STANDARD, 5)
                .withAvailableSteps(Integer.MAX_VALUE)
                .withCompleteScore(Integer.MAX_VALUE)
                .putWalls(new Integer[][]{{0, 0}})
                .fillBoard("xEEEE;GGGGG;BBBBB;RRRRR;OOOOO")
                .build();
    }

    /**
     * Test of getBoardState method, of class Level.
     */
    @Test
    public void testGetBoardState() {
        LOGGER.info("- Testing method getBoardState");

        String expected;
        String result;

        expected = "xEEEE;GGGGG;BBBBB;RRRRR;OOOOO";
        result = level.getBoardState();
        Assert.assertEquals(expected, result);

        level.transpose();
        expected = "xGBRO;EGBRO;EGBRO;EGBRO;EGBRO";
        result = level.getBoardState();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test of toString method, of class Level.
     */
    @Test
    public void testToString() {
        LOGGER.info("- Testing method toString");

        String expected
                = "[x,  ,  ,  ,  ]\n"
                + "[G, G, G, G, G]\n"
                + "[B, B, B, B, B]\n"
                + "[R, R, R, R, R]\n"
                + "[O, O, O, O, O]";
        String result = level.toString();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test of createCoordinates method, of class Level.
     */
    @Test
    public void testCreateCoordinates() {
        LOGGER.info("- Testing method createCoordinates");

        Integer[][] expected;

        expected = new Integer[][]{{0, 1}, {2, 3}, {4, 5}};
        Assert.assertArrayEquals(expected, Level.createCoordinates("0,1;2,3;4,5"));
        Assert.assertArrayEquals(expected, Level.createCoordinates(" 0,1 ; 2,3 ;       4,5 "));
        
        Assert.assertNull(Level.createCoordinates(""));
        Assert.assertNull(Level.createCoordinates("a,b;c,d;e,f"));
        Assert.assertNull(Level.createCoordinates("lorem ipsum"));
        Assert.assertNull(Level.createCoordinates("012345"));
        Assert.assertNull(Level.createCoordinates(null));
    }
}
