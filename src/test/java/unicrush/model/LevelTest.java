package unicrush.model;

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

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@code unicrush.model.Level}.
 *
 * @author Szalontai Jordán
 */
public class LevelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelTest.class);
    private static Level level;

    public LevelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Level");

        level = new Level.Builder(0, 5)
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
