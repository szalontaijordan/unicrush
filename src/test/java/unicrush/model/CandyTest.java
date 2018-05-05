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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with {@code JUnit} unit tests for the class {@code Candy}.
 *
 * @author Szalontai Jordán
 */
public class CandyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandyTest.class);
    
    private static final Candy.State[] COLOR_STATES = {
        Candy.State.BLUE,
        Candy.State.GREEN,
        Candy.State.ORANGE,
        Candy.State.PRUPLE,
        Candy.State.RED,
        Candy.State.YELLOW
    };

    public static List<Candy.State> colorStates;

    public CandyTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class Candy");
        colorStates = new ArrayList<>(Arrays.asList(COLOR_STATES));
    }

    /**
     * Test of isEmpty method, of class Candy.
     */
    @Test
    public void testIsEmpty() {
        LOGGER.info("- Testing method isEmpty");

        colorStates.stream()
                .forEach(state -> Assert.assertFalse(new Candy(state).isEmpty()));

        Candy empty = new Candy(Candy.State.EMPTY);
        Assert.assertTrue(empty.isEmpty());
    }

    /**
     * Test of getRandomColorState method, of class Candy.
     */
    @Test
    public void testGetRandomColorState() {
        LOGGER.info("- Testing method getRandomColorState");
        for (int i = 0; i < 10; i++) {
            Candy.State state = Candy.getRandomColorState();

            Assert.assertTrue(colorStates.contains(state));
        }
    }

    /**
     * Test of getStateFromChar method, of class Candy.
     */
    @Test
    public void testGetStateFromChar() {
        LOGGER.info("- Testing method getStateFromChar");

        char[] colorChars = "BGOPRY".toCharArray();

        for (int i = 0; i < colorStates.size(); i++) {
            Assert.assertEquals(colorStates.get(i),
                    Candy.getStateFromChar(colorChars[i]));
        }
        
        Assert.assertEquals(Candy.State.EMPTY, Candy.getStateFromChar('E'));
        Assert.assertEquals(null, Candy.getStateFromChar('x'));
    }

    /**
     * Test of compare method, of class Candy.
     */
    @Test
    public void testCompare() {
        LOGGER.info("- Testing method compare");

        int expResult;
        int result;

        Candy c1;
        Candy c2;

        // test 1
        c1 = new Candy(Candy.State.BLUE);
        c2 = new Candy(Candy.State.EMPTY);

        expResult = 1;
        result = Candy.compare(c1, c2);

        Assert.assertEquals(expResult, result);

        // test 2
        expResult = -1;
        result = Candy.compare(c2, c1);
        
        Assert.assertEquals(expResult, result);

        // test 3
        c2.setState(Candy.State.BLUE);

        expResult = 0;
        result = Candy.compare(c1, c2);

        Assert.assertEquals(expResult, result);

        // test 4
        c1.setState(Candy.State.RED);

        expResult = 0;
        result = Candy.compare(c1, c2);

        Assert.assertEquals(expResult, result);

        // test 5
        c1.setState(Candy.State.EMPTY);
        c2.setState(Candy.State.EMPTY);

        expResult = 0;
        result = Candy.compare(c1, c2);

        Assert.assertEquals(expResult, result);
    }
}
