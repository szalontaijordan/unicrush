package jordan.szalontai.unicrush;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

/**
 * Class with {@code JUnit} unit tests for the class {@code LevelManager}.
 *
 * @author Szalontai Jordán
 */
public class LevelManagerTest {

    public static final String NO_MATCH_STATE = ""
            + "xBGPx;"
            + "BGPRO;"
            + "GPROB;"
            + "PROBG;"
            + "xGRPx;";

    public static final String ALL_EMPTY_STATE = ""
            + "xEEEx;"
            + "EEEEE;"
            + "EEEEE;"
            + "EEEEE;"
            + "xEEEx;";

    public static final String CHAIN_REACTION_STATE = ""
            + "xBGGx;"
            + "BBBBR;"
            + "RYOGG;"
            + "RYOGG;"
            + "xOPOx;";

    private Level level;

    public LevelManagerTest() {
        this.level = null;
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("-- Testing class LevelManager");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("--");
    }

    @Before
    public void setUp() {
        try {
            level = new Level.Builder(LevelType.STANDARD, 8)
                .withCompleteScore(500)
                .withAvailableSteps(20)
                .putWalls("0,0;0,4;4,0;4,4")
                .fillBoard()
                .build();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        level = null;
    }

    /**
     * Test of popAllMarked method, of class LevelManager.
     */
    @Test
    public void testPopAllMarked() {
        System.out.println("-- -- Testing method: popAllMarked");

        boolean expResult;
        boolean result;

        // test 1
        level.set(1, 1, new Candy(Candy.State.BLUE));
        level.set(1, 2, new Candy(Candy.State.BLUE));
        level.set(1, 3, new Candy(Candy.State.BLUE));

        expResult = true;
        result = LevelManager.popAllMarked(level);

        Assert.assertEquals(expResult, result);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(NO_MATCH_STATE)
                .build();

        expResult = false;
        result = LevelManager.popAllMarked(level);

        Assert.assertEquals(expResult, result);

        // test 3
        level = null;

        try {
            LevelManager.popAllMarked(level);
        } catch (NullPointerException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Test of applyGravity method, of class LevelManager.
     */
    @Test
    public void testApplyGravity() {
        System.out.println("-- -- Testing method: applyGravity");

        long expResult;
        long result;

        // test 1
        level.set(1, 1, new Candy(Candy.State.EMPTY));
        level.set(1, 2, new Candy(Candy.State.EMPTY));
        level.set(1, 3, new Candy(Candy.State.EMPTY));

        expResult = 180L;
        result = LevelManager.applyGravity(level);

        Assert.assertEquals(result, expResult);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(ALL_EMPTY_STATE)
                .build();

        expResult = 21 / 3 * 21 * 60;
        result = LevelManager.applyGravity(level);

        Assert.assertEquals(expResult, result);

        // test 3
        level = new Level.Builder(level)
                .fillBoard(level.getInitialState())
                .build();

        expResult = 0L;
        result = LevelManager.applyGravity(level);

        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of processLevel method, of class LevelManager.
     */
    @Test
    public void testProcessLevel() {
        System.out.println("-- -- Testing method: processLevel");

        int expResult;
        int result;

        // test 1
        level = new Level.Builder(level)
                .fillBoard(CHAIN_REACTION_STATE)
                .build();

        expResult = 2;
        result = LevelManager.processLevel(level);

        Assert.assertTrue(result >= expResult);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(NO_MATCH_STATE)
                .build();

        expResult = 0;
        result = LevelManager.processLevel(level);

        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of processLevelWithState method, of class LevelManager.
     */
    @Test
    public void testProcessLevelWithState() {
        System.out.println("-- -- Testing method: processLevelWithState");

        long[] expResult;
        long[] result;

        // test 1
        level = new Level.Builder(level)
                .fillBoard(CHAIN_REACTION_STATE)
                .build();
        
        expResult = new long[]{ 2L, 360L };
        result = LevelManager.processLevelWithState(level, new ArrayList<>());
        
        Assert.assertTrue(result[0] >= expResult[0]);
        Assert.assertTrue(result[1] >= expResult[1]);
        
        // test 2
        level = new Level.Builder(level)
                .fillBoard(NO_MATCH_STATE)
                .build();
        
        expResult = new long[]{ 0L, 0L };
        result = LevelManager.processLevelWithState(level, new ArrayList<>());
        
        Assert.assertArrayEquals(expResult, result);
    }

    /**
     * Test of resetLevel method, of class LevelManager.
     */
    @Test
    public void testResetLevel() {
        System.out.println("-- -- Testing method: resetLevel");
        
        String initialState;
        String currentState;
        
        initialState = level.getBoardState();
        
        level.set(1, 1, new Candy(Candy.State.BLUE));
        level.set(1, 2, new Candy(Candy.State.BLUE));
        level.set(1, 3, new Candy(Candy.State.BLUE));
        
        LevelManager.processLevel(level);
        LevelManager.reset(level);
        
        currentState = level.getBoardState();
        
        Assert.assertEquals(initialState, currentState);
    }

}
