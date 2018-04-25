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

import java.util.Arrays;

/**
 * Class representing a {@code Candy} on the board.
 *
 * @author Szalontai Jordán
 */
public class Candy implements Comparable<Candy> {

    /**
     * The possible states of a {@code Candy}.
     */
    public static enum State {
        EMPTY, RED, GREEN, BLUE, ORANGE, PRUPLE, YELLOW
    }

    private State state;
    private boolean markedForPop;

    /**
     * Constructs a {@code Candy} with the given state.
     *
     * <p>
     * If the object is a colored candy, the parameter should be one of the
     * <em>color states</em></p>
     *
     * <pre>
     *   Candy.State.RED,
     *   Candy.State.GREEN,
     *   Candy.State.BLUE,
     *   Candy.State.ORANGE,
     *   Candy.State.PRUPLE,
     *   Candy.State.YELLOW
     * </pre>
     *
     * <p>
     * If you would like to represent the lack of a candy use</p>
     *
     * <pre>
     *   Candy.State.EMPTY
     * </pre>
     *
     * @param state the given state the candy will have
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
     * Returns a random color state.
     *
     * <p>
     * Available color states</p>
     *
     * <pre>
     *   Candy.State.RED,
     *   Candy.State.GREEN,
     *   Candy.State.BLUE,
     *   Candy.State.ORANGE,
     *   Candy.State.PRUPLE,
     *   Candy.State.YELLOW
     * </pre>
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
     * @return a {@code State} that starts with the given character
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

    public State getState() {
        return state;
    }

    public boolean isMarkedForPop() {
        return markedForPop;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setMarkedForPop(boolean markedForPop) {
        this.markedForPop = markedForPop;
    }

}
