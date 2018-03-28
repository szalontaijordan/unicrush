package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class containing static methods that manage the logics of the levels.
 *
 * @author Szalontai Jordán
 */
public abstract class LevelManager {

    /**
     * Refreshing the {@code Candy.State} of {@code Candy} instances in the
     * {@code level}'s board that are in a column or row with a length more than
     * two.
     *
     * @param l the {@code Level} which contains the board of {@code Candy}
     * instances
     * @return {@code true} if a change happened in the board, {@code false} if
     * did not
     */
    public static boolean popAllMarked(Level l) {
        markAllCandies(l);
        l.transpose();
        markAllCandies(l);
        l.transpose();

        boolean popHappened = false;

        for (int row = 0; row < l.getBoardSize(); row++) {
            for (int col = 0; col < l.getBoardSize(); col++) {
                if (l.get(row, col) != null && l.get(row, col).isMarkedForPop()) {
                    l.get(row, col).setState(Candy.State.EMPTY);
                    l.get(row, col).setMarkedForPop(false);
                    popHappened = true;
                }
            }
        }
        System.out.println(l);
        return popHappened;
    }

    /**
     * Marking each {@code Candy} instance as {@code markForPop} so the special
     * cases of pop can be solved.
     *
     * Using a {@code HashMap<String, Integer[]>} to keep count of the
     * occurrences of the instances.
     *
     * @param l the {@code Level} containing the board
     */
    private static void markAllCandies(Level l) {
        Map<String, Integer[]> candyCountMap = new HashMap<>();

        for (int i = 0; i < l.getBoardSize(); i++) {
            for (int j = 0; j < l.getBoardSize(); j++) {
                String curr = l.get(i, j) == null ? "" : l.get(i, j).toString();
                String next = l.get(i, j + 1) == null ? "" : l.get(i, j + 1).toString();

                if (!curr.equals("") && curr.equals(next)) {
                    if (candyCountMap.get(curr) == null) {
                        candyCountMap.put(curr, new Integer[]{2, j});
                    }
                    mark(l, i, candyCountMap.get(curr));
                } else {
                    candyCountMap.remove(curr);
                }
            }
            candyCountMap.clear();
        }
    }

    /**
     * Marking the {@code Candy} instances as {@code markForPop}.
     *
     * This is a helper method, using the value in the map.
     *
     * @param l the {@code Level} containing the board
     * @param row the row index of the sequence of {@code Candy} instances
     * waiting to be marked
     * @param v an array containing the length and the starting position of
     * candy-sequence
     */
    private static void mark(Level l, int row, Integer[] v) {
        if (v[0] >= 3) {
            for (int i = 0; i < v[0]; i++) {
                l.get(row, v[1] + i).setMarkedForPop(true);
            }
        }
        v[0]++;
    }

    /**
     * All {@code Candy} instances "fall" when there's empty space in their
     * column.
     *
     * @param l the {@code Level} which contains the board
     * @return 20 * {@code number of Candy instances}
     * in {@code Candy.State.EMPTY}
     */
    public static long applyGravity(Level l) {
        long re = 0;

        for (int i = 0; i < l.getBoardSize(); i++) {
            List<Candy> candies = new ArrayList<>();

            for (int col = 0; col < l.getBoardSize(); col++) {
                if (l.get(col, i) != null) {
                    candies.add(l.get(col, i));
                }
            }
            System.out.println(candies);
            Collections.sort(candies, Collections.reverseOrder());

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
            for (int col = 0; col < l.getBoardSize(); col++) {
                if (l.get(col, i) != null) {
                    l.set(col, i, candies.get(index++));
                }
            }
        }
        return re / 3 * re * 60;
    }

    /**
     * Iterations of popping the matching {@code Candy} instances and then
     * applying the gravity logic to the {@code Level}'s board.
     *
     * We check how many iterations occurred, so this cannot be an infinite
     * loop. It is important to be aware of the fact, that this method is
     * changing the model of the {@code Level} given as a parameter.
     *
     * @param l the {@code Level} on which we do these iterations
     * @return how many iterations occurred
     *
     * For further information see {@code popAllMarked}
     * and {@code applyGravity}.
     *
     */
    public static int processLevel(Level l) {
        int iterations = 0;
        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(l)) {
                applyGravity(l);
            } else {
                break;
            }
        }
        return iterations;
    }

    /**
     * Iterations of popping the matching {@code Candy} instances and then
     * applying the gravity logic to the {@code Level}'s board.
     *
     * We check how many iterations occurred, so this cannot be an infinite loop
     * and in addition we summarize the points that {@code applyGravity}
     * returns. It is important to be aware of the fact, that this method is
     * changing the model of the {@code Level} given as a parameter AND adding
     * the given {@code Level}'s {@code boardState String} to the given
     * {@code List<String>}.
     *
     * @param l the {@code Level} on which we do these iterations
     * @param states the {@code List<String>} that will contain
     * the {@code boardState Strings} after the iterations
     * @return a {@code long[]} array representing how many iterations occurred
     * and the sum of the points returned by {@code applyGravity}
     *
     * For further information see {@code popAllMarked}
     * and {@code applyGravity}
     */
    public static long[] processLevelWithState(Level l, List<String> states) {
        long iterations;
        long sum = 0;

        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(l)) {
                states.add(l.getBoardState());
                sum += applyGravity(l);
                states.add(l.getBoardState());
            } else {
                break;
            }
        }
        return new long[]{iterations, sum};
    }

    /**
     * Utilizes the {@code LevelBuilder} to reset all {@code Candy} instances in
     * a {@code Level}'s board.
     *
     * The method fills the {@code Level}'s board with the method implemented in
     * the concrete builder classes.
     *
     * @param l the {@code Level} we are rebuilding
     */
    public static void resetLevel(Level l) {
        try {
            LevelBuilder lb = new StandardLevelBuilder();

            if (l instanceof StandardLevel) {
                lb = new StandardLevelBuilder(l);
            } // ...

            l = lb.fillBoard().create();

            processLevel(l);
        } catch (Exception ex) {
        }
    }
}
