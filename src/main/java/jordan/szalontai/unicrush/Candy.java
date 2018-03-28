package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 * Class for representing a {@code Candy} on the board.
 *
 * @author Szalontai Jordán
 */
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

    private State state;
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

    public State getState() {
        return state;
    }

    public boolean isMarkedForPop() {
        return markedForPop;
    }
    
    public void setMarkedForPop(boolean mark) {
        this.markedForPop = mark;
    }

    public void setState(State color) {
        this.state = color;
    }

    /**
     * Decides if the {@code Candy} is in the {@code State.EMPTY} state.
     *
     * @return {@code true} if the {@code Candy} is in the {@code State.EMPTY} state,
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return this.state == State.EMPTY;
    }

    @Override
    public String toString() {
        return state.toString().charAt(0) + "";
    }

    @Override
    public int compareTo(Candy o) {
        if (o.isEmpty()) {
            return -1;
        }
        return 0;
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
    
}
