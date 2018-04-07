package jordan.szalontai.unicrush;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a {@code Candy} on the board.
 *
 * @author Szalontai Jordán
 */
@Getter
@Setter
public class Candy implements Comparable<Candy> {

    /**
     * The possible states of a {@code Candy}.
     */
    public static enum State {
        EMPTY,
        RED,
        GREEN,
        BLUE,
        ORANGE,
        PRUPLE,
        YELLOW
    }

    /**
     * Indicates the {@code State} of this object.
     *
     * @param state the new state of this object
     * @return the state of this object
     */
    private State state;

    /**
     * Indicates if this object is marked for removal.
     *
     * @param markedForPop the new value that indicates if this object is marked
     * for removal
     * @return true if this object is marked for removal false
     * otherwise
     */
    private boolean markedForPop;

    /**
     * Constructs a {@code Candy} with the given state.
     *
     * @param state the given state which the candy will be in
     */
    public Candy(State state) {
        this.state = state;
    }

    /**
     * Constructs a {@code Candy} as a copy of the given parameter.
     *
     * @param src the source that we are copying
     */
    public Candy(Candy src) {
        this.state = src.state;
        this.markedForPop = false;
    }

    /**
     * Decides if the {@code Candy} is in the {@code State.EMPTY} state.
     *
     * @return {@code true} if the {@code Candy} is in the {@code State.EMPTY}
     * state, {@code false} otherwise
     */
    public boolean isEmpty() {
        return this.state == State.EMPTY;
    }

    @Override
    public String toString() {
        return state.toString().charAt(0) + "";
    }

    @Override
    public int compareTo(Candy otherCandy) {
        return compare(this, otherCandy);
    }

    /**
     * Returns a random state that represents the state candy in this special
     * case.
     *
     * @return a {@code Candy.State} representing the state of the candy
     */
    public static State getRandomColorState() {
        return Arrays.asList(State.values())
                .get((int) (Math.random() * (State.values().length - 1)) + 1);
    }

    /**
     * Returns the {@code State} that's string representation starts with the
     * given parameter.
     *
     * @param c the first character of the desired {@code State}'s string
     * representation
     * @return
     */
    public static State getStateFromChar(char c) {
        return Arrays.stream(State.values())
                .filter(s -> s.toString().charAt(0) == c)
                .findFirst()
                .orElse(null);
    }

    /**
     * Comparing method for two {@code Candy} instances.
     *
     * <p>
     * The purpose of this method is that the instances with the
     * {@code State.EMPTY} state will be first in a list or an array if a sort
     * occurs to it.</p>
     *
     * <p>
     * The basic concept is, that we map an integer value to each {@code Candy}
     * like this:</p>
     *
     * <pre>
     *     Candy in State.EMPTY --&gt; -1,
     *     Candy in other State --&gt;  1
     * </pre>
     *
     * <p>
     * So a collection of {@code Candy} instances act like a list of integers
     * and the negative numbers will be first</p>
     *
     * @param firstCandy the first {@code Candy}
     * @param secondCandy the second {@code Candy}
     * @return 0, 1, -1 based on the {@code compare} method of the
     * {@code Integer} class because of the example above
     */
    public static int compare(Candy firstCandy, Candy secondCandy) {
        int first = 1;
        int second = 1;

        if (firstCandy.state == State.EMPTY) {
            first = -1;
        }

        if (secondCandy.state == State.EMPTY) {
            second = -1;
        }

        return Integer.compare(first, second);
    }

}
