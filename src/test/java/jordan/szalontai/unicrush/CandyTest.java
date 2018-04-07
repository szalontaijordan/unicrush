package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class with {@code JUnit} unit tests for the class {@code Candy}.
 *
 * @author Szalontai Jordán
 */
public class CandyTest {

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
        System.out.println("-- Testing class Candy");
        colorStates = new ArrayList<>(Arrays.asList(COLOR_STATES));
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("--");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isEmpty method, of class Candy.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("-- -- Testing method isEmpty");

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
        System.out.println("-- -- Testing method getRandomColorState");
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
        System.out.println("-- -- Testing method getStateFromChar");

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
        System.out.println("-- -- Testing method compare");

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
