package unicrush.model;

import java.util.List;
import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@code unicrush.model.LevelManager}.
 *
 * @author szalontaijordan
 */
public class LevelManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelManagerTest.class);
    private static final int BOARD_SIZE = 5;

    public static final String ALL_EMPTY_LEVEL
            = "xEEEx;"
            + "EEEEE;"
            + "EExEE;"
            + "EEEEE;"
            + "xEEEx";

    public static final String NO_MATCH_LEVEL
            = "xRGBx;"
            + "RGBRG;"
            + "GPxPO;"
            + "OPGRB;"
            + "xBOPx";

    public static final String INLINE_ROW_MATCH_FIRST_LEVEL
            = "xRGBx;"
            + "BBRBG;"
            + "GRxPO;"
            + "OPGRB;"
            + "xBOPx";

    public static final String INLINE_COL_MATCH_FIRST_LEVEL
            = "xRGBx;"
            + "RRBRG;"
            + "GPxPO;"
            + "ORGRB;"
            + "xBOPx";

    public static final String BOX_ROW_MATCH_FIRST_LEVEL
            = "xGGBx;"
            + "ROBRG;"
            + "GRxPO;"
            + "OGGRB;"
            + "xBOPx";

    public static final String BOX_COL_MATCH_FIRST_LEVEL
            = "xRGBx;"
            + "RGBPG;"
            + "GRxPO;"
            + "OPGRO;"
            + "xBOGx";

    public static final String CHAIN_REACTION_LEVEL
            = "xRGBx;"
            + "RBBRG;"
            + "GBxPO;"
            + "OOORB;"
            + "xBGPx";

    public static final String CHAIN_REACTION_LEVEL_AFTER
            = "x..Bx;"
            + ".RGRG;"
            + "RBxPO;"
            + "GBBRB;"
            + "xBGPx";

    private static Level.Builder builder;

    private LevelManager manager;

    public LevelManagerTest() {
        this.manager = new LevelManager();
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class LevelManager");

        String template = "0,0;4,0;0,4;4,4;2,2";
        builder = new Level.Builder(0, Level.Type.STANDARD, BOARD_SIZE)
                .withCompleteScore(Integer.MAX_VALUE)
                .withAvailableSteps(Integer.MAX_VALUE)
                .putWalls(Level.createCoordinates(template));
    }

    /**
     * Test of process method, of class LevelManager.
     */
    @Test
    public void testProcess() {
        LOGGER.info("- Testing method process");
        int iterations;

        manager.setLevel(builder.fillBoard(ALL_EMPTY_LEVEL).build());
        iterations = manager.process();
        Assert.assertTrue(iterations >= 1);

        manager.setLevel(builder.fillBoard(NO_MATCH_LEVEL).build());
        iterations = manager.process();
        Assert.assertEquals(0, iterations);

        manager.setLevel(builder.fillBoard(CHAIN_REACTION_LEVEL).build());
        iterations = manager.process();
        Assert.assertTrue(iterations >= 2);
    }

    /**
     * Test of processWithState method, of class LevelManager.
     */
    @Test
    public void testProcessWithState() {
        LOGGER.info("- Testing method processWithState");
        List<String> boardStates;

        manager.setLevel(builder.fillBoard(ALL_EMPTY_LEVEL).build());
        boardStates = manager.processWithState();
        Assert.assertEquals(ALL_EMPTY_LEVEL, boardStates.get(0));
        Assert.assertTrue(manager.getIterations() >= 1);

        manager.setLevel(builder.fillBoard(NO_MATCH_LEVEL).build());
        boardStates = manager.processWithState();
        Assert.assertTrue(boardStates.isEmpty());
        Assert.assertEquals(0, manager.getIterations());

        manager.setLevel(builder.fillBoard(CHAIN_REACTION_LEVEL).build());
        boardStates = manager.processWithState();
        Assert.assertEquals(CHAIN_REACTION_LEVEL.replace("OOO", "EEE"), boardStates.get(0));
        Assert.assertTrue(boardStates.get(1).matches(CHAIN_REACTION_LEVEL_AFTER));
        Assert.assertTrue(manager.getIterations() >= 2);
    }

    /**
     * Test of reset method, of class LevelManager.
     */
    @Test
    public void testReset() {
        LOGGER.info("- Testing method reset");

        String before;
        String after;

        manager.setLevel(builder.fillBoard().build());

        // not using any LevelManager method to manipulate the board!
        for (int i = 0; i < 100; i++) {
            int randI = (int) (Math.random() * BOARD_SIZE);
            int randJ = (int) (Math.random() * BOARD_SIZE);

            manager.getLevel().set(randI, randJ, new Candy(Candy.getRandomColorState()));
        }
        manager.reset();

        before = manager.getLevel().getInitialState();
        after = manager.getLevel().getBoardState();
        Assert.assertEquals(before, after);
    }

    /**
     * Test of swap method, of class LevelManager.
     */
    @Test
    public void testSwap() {
        LOGGER.info("- Testing method swap");

        boolean isSuccesfulSwap;
        manager.setLevel(builder.fillBoard(INLINE_ROW_MATCH_FIRST_LEVEL).build());

        isSuccesfulSwap = manager.swap(new Integer[][]{{1, 2}, {1, 3}});
        Assert.assertTrue(isSuccesfulSwap);

        isSuccesfulSwap = manager.swap(new Integer[][]{{1, 3}, {1, 2}});
        Assert.assertTrue(isSuccesfulSwap);

        isSuccesfulSwap = manager.swap(new Integer[][]{{0, 2}, {1, 3}});
        Assert.assertFalse(isSuccesfulSwap);

        isSuccesfulSwap = manager.swap(new Integer[][]{{1, 2}, {1, 2}});
        Assert.assertFalse(isSuccesfulSwap);
    }

    /**
     * Test of getAvailableMoves method, of class LevelManager.
     */
    @Test
    public void testGetAvailableMoves() {
        LOGGER.info("- Testing method getAvailableMoves");

        manager.setLevel(builder.fillBoard(NO_MATCH_LEVEL).build());
        Assert.assertEquals("", manager.getAvailableMoves());

        manager.setLevel(builder.fillBoard(INLINE_ROW_MATCH_FIRST_LEVEL).build());
        Assert.assertEquals("1,0;1,1;1,2;1,3;", manager.getAvailableMoves());

        manager.setLevel(builder.fillBoard(INLINE_COL_MATCH_FIRST_LEVEL).build());
        Assert.assertEquals("1,0;1,1;1,2;1,3;", manager.getAvailableMoves());

        manager.setLevel(builder.fillBoard(BOX_ROW_MATCH_FIRST_LEVEL).build());
        Assert.assertEquals("2,0;3,0;2,1;3,1;2,2;3,2;", manager.getAvailableMoves());

        manager.setLevel(builder.fillBoard(BOX_COL_MATCH_FIRST_LEVEL).build());
        Assert.assertEquals("0,0;0,1;1,0;1,1;2,0;2,1;", manager.getAvailableMoves());
    }

    /**
     * Test of popAllMarked method, of class LevelManager.
     */
    @Test
    public void testPopAllMarked() {
        LOGGER.info("- Testing method popAllMarked");

        boolean isSuccessfulPop;

        manager.setLevel(builder.fillBoard(CHAIN_REACTION_LEVEL).build());
        isSuccessfulPop = manager.popAllMarked();
        Assert.assertTrue(isSuccessfulPop);

        manager.setLevel(builder.fillBoard(NO_MATCH_LEVEL).build());
        isSuccessfulPop = manager.popAllMarked();
        Assert.assertFalse(isSuccessfulPop);
    }

    /**
     * Test of applyGravity method, of class LevelManager.
     */
    @Test
    public void testApplyGravity() {
        LOGGER.info("- Testing method applyGravity");

        int sum;

        manager.setLevel(builder.fillBoard(ALL_EMPTY_LEVEL).build());
        sum = manager.applyGravity();
        Assert.assertEquals(20 / 3 * 20 * 60, sum);

        manager.setLevel(builder.fillBoard(NO_MATCH_LEVEL).build());
        sum = manager.applyGravity();
        Assert.assertEquals(0, sum);

        manager.setLevel(builder.fillBoard(CHAIN_REACTION_LEVEL.replace("OOO", "EEE")).build());
        sum = manager.applyGravity();
        Assert.assertEquals(3 / 3 * 3 * 60, sum);
    }
}
