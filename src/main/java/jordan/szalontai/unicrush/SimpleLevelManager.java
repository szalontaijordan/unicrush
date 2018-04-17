package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class containing methods that manage the logics of the levels.
 *
 * @author Szalontai Jordán
 */
public class SimpleLevelManager implements LevelManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLevelManager.class);

    /**
     * The singleton instance of this class.
     */
    public static SimpleLevelManager instance;

    private SimpleLevelManager() {
    }

    private void markAllCandies(Level level) {
        Map<String, Integer[]> candyCountMap = new HashMap<>();

        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                String curr = level.get(i, j) == null ? "" : level.get(i, j).toString();
                String next = level.get(i, j + 1) == null ? "" : level.get(i, j + 1).toString();

                if (!curr.equals("") && curr.equals(next)) {
                    if (candyCountMap.get(curr) == null) {
                        candyCountMap.put(curr, new Integer[]{2, j});
                    }
                    mark(level, i, candyCountMap.get(curr));
                } else {
                    candyCountMap.remove(curr);
                }
            }
            candyCountMap.clear();
        }
    }

    private void mark(Level level, int row, Integer[] v) {
        if (v[0] >= 3) {
            for (int i = 0; i < v[0]; i++) {
                level.get(row, v[1] + i).setMarkedForPop(true);
            }
        }
        v[0]++;
    }

    private String inlineMatch(String top3, String row, int i, Level level) {
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

    private String allMatch(String top3, String bot3, int i, int j, Level level) {
        String coor = "";
        String[] regexes = {
            "(XX...X|X.X.X.|.XXX..)".replaceAll("X", "" + top3.charAt(0)),
            "(..XXX.|.X.X.X|X...XX)".replaceAll("X", "" + top3.charAt(0)),
            "(XX...X|X.X.X.|.XXX..)".replaceAll("X", "" + bot3.charAt(0)),
            "(..XXX.|.X.X.X|X...XX)".replaceAll("X", "" + bot3.charAt(0))
        };

        for (String regex : regexes) {
            LOGGER.debug("{}", regex);
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
     * Returns a template string containing the coordinates of a box, in which a
     * move is possible.
     *
     * <p>
     * This method finds a single possible move. More specific, the one
     * horizontal box, which is the closest to the top of the level.</p>
     *
     * @param level the level we search in
     * @return a string representing the coordinates of a box in which a move is
     * possible
     */
    public String areThereAvailableMoves(Level level) {
        String possibleCoordinates = "";

        LOGGER.debug("Horizontal processing..");
        possibleCoordinates = lookForMoves(level);

        if (possibleCoordinates.length() == 0) {
            level.transpose();

            LOGGER.debug("Vertical processing..");
            possibleCoordinates += lookForMoves(level);
            level.transpose();
        }

        return possibleCoordinates;
    }

    /**
     * Returns a template string with coordinates of a box area, in which a move
     * is possible.
     *
     * <p>
     * The first step is that we look for a possible move in a row. For
     * example</p>
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
     * If there is none in a row like above, we look for a 3x2 rectangle, in
     * which we can perform a move.</p>
     *
     * @param level the level we search in
     * @return a string representing a coordinate, we found first with the
     * algorithm above
     */
    public String lookForMoves(Level level) {
        String coor = "";
        String[] levelStates = level.getBoardState().split(";");

        LOGGER.debug("{}", Arrays.toString(levelStates));

        for (int i = 0; i < levelStates.length - 1; i++) {
            for (int j = 0; j < levelStates[i].length() - 3; j++) {
                String top3 = levelStates[i].substring(j, j + 3);
                String bot3 = levelStates[i + 1].substring(j, j + 3);

                coor = inlineMatch(top3, levelStates[i], i, level);

                if (coor.length() == 0) {
                    coor = allMatch(top3, bot3, i, j, level);
                }

                if (coor.length() != 0) {
                    return coor;
                }
            }
        }
        return coor;
    }

    /**
     * Refreshes the {@code Candy.State} of {@code Candy} instances in the
     * {@code level}'s board that are in a column or row with a length more than
     * two.
     *
     * @param level the {@code Level} which contains the board of {@code Candy}
     * instances
     * @return {@code true} if a change happened in the board, {@code false} if
     * did not
     */
    public boolean popAllMarked(Level level) {
        markAllCandies(level);
        level.transpose();
        markAllCandies(level);
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
     * All {@code Candy} instances "fall" when there's empty space in their
     * column.
     *
     * @param level the {@code Level} which contains the board
     * @return 60 multiplied by the {@code number of Candy instances} in
     * {@code Candy.State.EMPTY}, multiplied by the {@code number of blocks}
     * (one block = three or more {@code Candy} instances in a row or column)
     */
    public long applyGravity(Level level) {
        long re = 0;

        for (int i = 0; i < level.getBoardSize(); i++) {
            List<Candy> candies = new ArrayList<>();

            for (int col = 0; col < level.getBoardSize(); col++) {
                if (level.get(col, i) != null) {
                    candies.add(level.get(col, i));
                }
            }

            LOGGER.trace("Candies in active column:\n{}", candies.toString());
            Collections.sort(candies);

            re += candies.stream()
                    .filter(c -> c.isEmpty())
                    .count();

            candies.replaceAll(c -> {
                if (c.isEmpty()) {
                    return new Candy(Candy.getRandomColorState());
                }
                return c;
            });

            int index = 0;
            for (int col = 0; col < level.getBoardSize(); col++) {
                if (level.get(col, i) != null) {
                    level.set(col, i, candies.get(index++));
                }
            }
        }
        return re / 3 * re * 60;
    }

    @Override
    public int process(Level level) {
        int iterations;
        for (iterations = 0; iterations < CandyCrushGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(level)) {
                applyGravity(level);
            } else {
                break;
            }
        }
        return iterations;
    }

    @Override
    public List<String> processWithState(Level level) {
        List<String> states = new ArrayList<>();
        long iterations;
        long sum = 0;

        for (iterations = 0; iterations < CandyCrushGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(level)) {
                states.add(level.getBoardState());
                sum += applyGravity(level);
                states.add(level.getBoardState());
            } else {
                break;
            }
        }

        states.add(0, iterations + "," + sum);
        return states;
    }

    public synchronized static SimpleLevelManager getInstance() {
        if (instance == null) {
            instance = new SimpleLevelManager();
        }

        return instance;
    }

}
