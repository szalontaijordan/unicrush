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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class containing methods that manage the logics of the levels.
 *
 * @author Szalontai Jordán
 */
public final class LevelManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelManager.class);

    private Level level;
    private int iterations;
    private int sum;

    /**
     * Constructs an object for a game, that can make changes to the logic of the level.
     *
     * @param level the level we would like to manage
     */
    public LevelManager(Level level) {
        this.level = level;
        this.iterations = 0;
        this.sum = 0;
    }

    /**
     * Constructs an object for a game, with no level to manage.
     */
    public LevelManager() {
        this.level = null;
        this.iterations = 0;
        this.sum = 0;
    }

    /**
     * Iterations processing the level and returning the score.
     *
     * <p>
     * Iterations should consist of popping the matching {@code Candy} instances and then applying
     * the gravity logic to the {@code Level}'s board.
     *
     * <p>
     * We check how many iterations occurred, so this cannot be an infinite loop</p>
     *
     * @return how many iterations occurred
     */
    public int process() {
        for (iterations = 0; iterations < CandyCrushGame.MAX_ITERATION; iterations++) {
            if (popAllMarked()) {
                sum += applyGravity();
            } else {
                break;
            }
        }
        return iterations;
    }

    /**
     * Iterations processing the level and returning information in a list.
     *
     * <p>
     * Iterations should consist of popping the matching {@code Candy} instances and then applying
     * the gravity logic to the {@code Level}'s board.
     * </p>
     * <p>
     * We should check how many iterations occurred, so this cannot be an infinite loop and in
     * addition we summarize the points that {@code applyGravity} returns.</p>
     *
     * @return a list containing the board states of each iteration
     */
    public List<String> processWithState() {
        List<String> states = new ArrayList<>();
        sum = 0;

        for (iterations = 0; iterations < CandyCrushGame.MAX_ITERATION; iterations++) {
            if (popAllMarked()) {
                states.add(level.getBoardState());
                sum += applyGravity();
                states.add(level.getBoardState());
            } else {
                break;
            }
        }

        return states;
    }

    /**
     * Resetting the {@code board} based on the original board state.
     */
    public void reset() {
        String[] boardStates = level.getInitialState().split(";", level.getBoardSize());
        for (int i = 0; i < boardStates.length; i++) {
            for (int j = 0; j < boardStates[i].length(); j++) {
                if (boardStates[i].charAt(j) == 'x') {
                    level.set(i, j, null);
                } else {
                    level.set(i, j, new Candy(Candy.getStateFromChar(boardStates[i].charAt(j))));
                }
            }
        }

        if (level.isTransposed()) {
            level.transpose();
        }
    }

    /**
     * Swaps two {@code Candy} instances on the board, if a given statement is true.
     *
     * @param coors an array representing the coordinates of the {@code Candy} instances in the
     * board
     * @return {@code true} if swap was successful, {@code false} if not
     */
    public boolean swap(Integer[][] coors) {
        if (Math.abs(coors[0][0] - coors[1][0]) + Math.abs(coors[0][1] - coors[1][1]) != 1) {
            return false;
        }
        if (level.get(coors[0][0], coors[0][1]) == null || level.get(coors[1][0], coors[1][1]) == null) {
            return false;
        }

        Candy tmp = level.get(coors[0][0], coors[0][1]);
        level.set(coors[0][0], coors[0][1], level.get(coors[1][0], coors[1][1]));
        level.set(coors[1][0], coors[1][1], tmp);

        return true;
    }

    /**
     * Returns a template string containing the coordinates of a box, in which a move is possible.
     *
     * <p>
     * This method looks for available moves in the following order:</p>
     * <ol>
     * <li>row + inline</li>
     * <li>row + box</li>
     * <li>column + inline</li>
     * <li>column + box</li>
     * </ol>
     *
     * @return a string representing the coordinates of a box in which a move is possible
     */
    public String getAvailableMoves() {
        String possibleCoordinates = "";

        LOGGER.debug("Horizontal processing..");
        possibleCoordinates = lookForMoves();

        if (possibleCoordinates.length() == 0) {
            level.transpose();

            LOGGER.debug("Vertical processing..");
            possibleCoordinates = lookForMoves();
            level.transpose();
        }

        return possibleCoordinates;
    }

    /**
     * Returns a template string with coordinates of a rectangle area, in which a move is possible.
     *
     * <p>
     * The first step is that we look for a possible move in a row. For example</p>
     * <pre>
     *   Let a level's toString be equal to
     *
     *     [x, B, B, G, x]
     *     [B, B, G, B, x]
     *     [x, P, x, x, x]
     *     [x, x, x, x, x]
     *     [x, x, x, x, x],
     *
     *   in this case the return value is
     *
     *     1,0;1,1;1,2;1,3;
     * </pre>
     * <p>
     * If there are no possible moves inline, we look for a 3x2 rectangle, in which we can perform a
     * move.</p>
     *
     * @return a string representing a coordinate, we found first with the algorithm above
     */
    public String lookForMoves() {
        String coor = "";
        String[] levelStates = level.getBoardState().split(";");

        for (int i = 0; i < levelStates.length - 1; i++) {
            for (int j = 0; j < levelStates[i].length() - 3; j++) {
                String top3 = levelStates[i].substring(j, j + 3);
                String bot3 = levelStates[i + 1].substring(j, j + 3);

                coor = inlineMatch(top3, levelStates[i], i);

                if (coor.length() != 0) {
                    return coor;
                } else {
                    coor = reactangleMatch(top3, bot3, i, j);
                }

                if (coor.length() != 0) {
                    return coor;
                } else {
                    continue;
                }
            }
        }
        return coor;
    }

    private String inlineMatch(String top3, String row, int i) {
        String coor = "";
        String reg = "(.*XX.X.*|.*X.XX.*)".replaceAll("X", "" + top3.charAt(0));

        if (row.matches(reg)) {
            int start = row.indexOf(top3.charAt(0));
            for (int k = start; k < start + 4; k++) {
                if (level.isTransposed()) {
                    coor += k + "," + i + ";";
                } else {
                    coor += i + "," + k + ";";
                }
            }
            return coor;
        }
        return coor;
    }

    private String reactangleMatch(String top3, String bot3, int i, int j) {
        String coor = "";
        String[] regexes = {
            "(XX...X|X.X.X.|.XXX..)".replaceAll("X", "" + top3.charAt(0)),
            "(..XXX.|.X.X.X|X...XX)".replaceAll("X", "" + top3.charAt(0)),
            "(XX...X|X.X.X.|.XXX..)".replaceAll("X", "" + bot3.charAt(0)),
            "(..XXX.|.X.X.X|X...XX)".replaceAll("X", "" + bot3.charAt(0))
        };

        for (String regex : regexes) {
            if ((top3 + bot3).matches(regex) || (bot3 + top3).matches(regex)) {
                LOGGER.debug("Parsing:\n{}\n{}", top3, bot3);

                for (int k = j; k < j + 3; k++) {
                    if (level.isTransposed()) {
                        coor += k + "," + i + ";";
                        coor += k + "," + (i + 1) + ";";
                    } else {
                        coor += i + "," + k + ";";
                        coor += (i + 1) + "," + k + ";";
                    }
                }
                return coor;
            }
        }
        return coor;
    }

    /**
     * Refreshes the {@code Candy.State} of {@code Candy} instances in the {@code level}'s board
     * that are in a column or row with a length more than two.
     *
     * @return {@code true} if a change happened in the board, {@code false} if did not
     */
    public boolean popAllMarked() {
        markAllCandies();
        level.transpose();
        markAllCandies();
        level.transpose();

        boolean popHappened = false;

        for (int row = 0; row < level.getBoardSize(); row++) {
            for (int col = 0; col < level.getBoardSize(); col++) {
                if (level.get(row, col) != null && level.get(row, col).isMarkedForPop()) {
                    level.get(row, col).setState(Candy.State.EMPTY);
                    level.get(row, col).setMarkedForPop(false);
                    popHappened = true;
                }
            }
        }

        LOGGER.trace("Current level:\n{}", level.toString());
        return popHappened;
    }

    /**
     * All {@code Candy} instances "fall" when there's empty space in their column.
     *
     * @return the score that is calculated by the following formula:
     * <pre>EMPTY_CANDIES / 3 * EMPTY_CANDIES * 60</pre>
     */
    public int applyGravity() {
        int re = 0;

        for (int i = 0; i < level.getBoardSize(); i++) {
            List<Candy> candies = new ArrayList<>();

            for (int col = 0; col < level.getBoardSize(); col++) {
                if (level.get(col, i) == null) {
                    continue;
                }
                candies.add(level.get(col, i));

                if (level.get(col, i).isEmpty()) {
                    re++;
                }
            }

            LOGGER.trace("Candies in active column:\n{}", candies.toString());

            Collections.sort(candies);
            candies.replaceAll(c -> c.isEmpty() ? new Candy(Candy.getRandomColorState()) : c);

            int index = 0;
            for (int col = 0; col < level.getBoardSize(); col++) {
                if (level.get(col, i) != null) {
                    level.set(col, i, candies.get(index++));
                }
            }
        }
        return re / 3 * re * 60;
    }

    // TODO: refactor this method!
    //       maybe regexes ??
    private void markAllCandies() {
        Map<String, Integer[]> candyCountMap = new HashMap<>();

        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                String curr = level.get(i, j) == null ? "" : level.get(i, j).toString();
                String next = level.get(i, j + 1) == null ? "" : level.get(i, j + 1).toString();

                if (!curr.equals("") && curr.equals(next)) {
                    if (candyCountMap.get(curr) == null) {
                        candyCountMap.put(curr, new Integer[]{2, j});
                    }
                    mark(i, candyCountMap.get(curr));
                } else {
                    candyCountMap.remove(curr);
                }
            }
            candyCountMap.clear();
        }
    }

    private void mark(int row, Integer[] columns) {
        if (columns[0] >= 3) {
            for (int i = 0; i < columns[0]; i++) {
                level.get(row, columns[1] + i).setMarkedForPop(true);
            }
        }
        columns[0]++;
    }

    public int getIterations() {
        return iterations;
    }

    public int getSum() {
        return sum;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        // just to make sure reset everything
        this.iterations = 0;
        this.sum = 0;
        this.level = level;
    }
}
