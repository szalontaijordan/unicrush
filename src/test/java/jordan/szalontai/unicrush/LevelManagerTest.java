package jordan.szalontai.unicrush;

/*-
 * #%L
 * unicrush
 * %%
 * Copyright (C) 2018 Faculty of Informatics
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import unicrush.controller.SimpleLevelManager;
import unicrush.model.Level;
import unicrush.model.Candy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

/**
 * Class with {@code JUnit} unit tests for the class {@code SimpleLevelManager.getInstance()}.
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
            level = new Level.Builder(Level.Type.STANDARD, 8)
                .withCompleteScore(500)
                .withAvailableSteps(20)
                .putWalls(Level.createCoordinates("0,0;0,4;4,0;4,4"))
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
     * Test of popAllMarked method, of class SimpleLevelManager.getInstance().
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
        result = SimpleLevelManager.getInstance().popAllMarked(level);

        Assert.assertEquals(expResult, result);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(NO_MATCH_STATE)
                .build();

        expResult = false;
        result = SimpleLevelManager.getInstance().popAllMarked(level);

        Assert.assertEquals(expResult, result);

        // test 3
        level = null;

        try {
            SimpleLevelManager.getInstance().popAllMarked(level);
        } catch (NullPointerException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Test of applyGravity method, of class SimpleLevelManager.getInstance().
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
        result = SimpleLevelManager.getInstance().applyGravity(level);

        Assert.assertEquals(result, expResult);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(ALL_EMPTY_STATE)
                .build();

        expResult = 21 / 3 * 21 * 60;
        result = SimpleLevelManager.getInstance().applyGravity(level);

        Assert.assertEquals(expResult, result);

        // test 3
        level = new Level.Builder(level)
                .fillBoard(level.getInitialState())
                .build();

        expResult = 0L;
        result = SimpleLevelManager.getInstance().applyGravity(level);

        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of process method, of class SimpleLevelManager.getInstance().
     */
    @Test
    public void testProcess() {
        System.out.println("-- -- Testing method: process");

        int expResult;
        int result;

        // test 1
        level = new Level.Builder(level)
                .fillBoard(CHAIN_REACTION_STATE)
                .build();

        expResult = 2;
        result = SimpleLevelManager.getInstance().process(level);

        Assert.assertTrue(result >= expResult);

        // test 2
        level = new Level.Builder(level)
                .fillBoard(NO_MATCH_STATE)
                .build();

        expResult = 0;
        result = SimpleLevelManager.getInstance().process(level);

        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of processWithState method, of class SimpleLevelManager.getInstance().
     */
    @Test
    public void testProcessLevelWithState() {
        System.out.println("-- -- Testing method: processWithState");
        // TODO
    }

    // TODO other public methods

}
