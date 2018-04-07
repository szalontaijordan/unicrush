package jordan.szalontai.unicrush;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    private String initialState;

    public LevelManagerTest() {
        this.level = null;
        this.initialState = "";
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
            level = new StandardLevelBuilder()
                    .setCompliteScore(500)
                    .setAvailableSteps(20)
                    .setBoardSize(5)
                    .putWallsFromString("0,0;0,4;4,0;4,4")
                    .fillBoard()
                    .create();
            initialState = level.getBoardState();
        } catch (Exception e) {
            fail(e.getMessage());
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

        assertEquals(expResult, result);

        // test 2
        level = new StandardLevelBuilder(level)
                .setBoardFromState(NO_MATCH_STATE)
                .create();

        expResult = false;
        result = LevelManager.popAllMarked(level);

        assertEquals(expResult, result);

        // test 3
        level = null;
        
        try {
            LevelManager.popAllMarked(level);
        } catch (NullPointerException e) {
            assertTrue(true);
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
        
        assertEquals(result,expResult);
        
        // test 2
        level = new StandardLevelBuilder(level)
                .setBoardFromState(ALL_EMPTY_STATE)
                .create();
        
        expResult = 21 / 3 * 21 * 60;
        result = LevelManager.applyGravity(level);
        
        assertEquals(expResult, result);
        
        // test 3
        level = new StandardLevelBuilder(level)
                .setBoardFromState(initialState)
                .create();
        
        expResult = 0L;
        result = LevelManager.applyGravity(level);
        
        assertEquals(expResult, result);
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
        level = new StandardLevelBuilder(level)
                .setBoardFromState(CHAIN_REACTION_STATE)
                .create();
        
        expResult = 2;
        result = LevelManager.processLevel(level);
        
        assertTrue(result >= expResult);
        
        // test 2
        level = new StandardLevelBuilder(level)
                .setBoardFromState(NO_MATCH_STATE)
                .create();
        
        expResult = 0;
        result = LevelManager.processLevel(level);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of processLevelWithState method, of class LevelManager.
     */
    @Test
    public void testProcessLevelWithState() {
        // TODO
    }

    /**
     * Test of resetLevel method, of class LevelManager.
     */
    @Test
    public void testResetLevel() {
        // TODO
    }

}
