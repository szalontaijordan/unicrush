package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LevelManager {

    /**
     * Replaces the color of candies that are in a column or row with a length
     * more than 2.
     *
     * @param l the level which consists the board of candies
     * @return true if a change happened in the board, false if did not
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
                    l.get(row, col).setColor(Candy.Color.EMPTY);
                    l.get(row, col).setMarkedForPop(false);
                    popHappened = true;
                }
            }
        }
        System.out.println(l);
        return popHappened;
    }

    /**
     * Marking each candy with {@code markForPop} so the special cases of pop
     * can be solved.
     *
     * Using a {@code HashMap<String, Integer>} to keep count of the occurrence
     * of the candies.
     *
     * @param l the level containing the board
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
     * Marking the candies {@code markForPop}.
     *
     * This is a helper method, using the value in the map. @see
     * {@code punchLevel}
     *
     * @param l the level containing the board
     * @param row the row index of the candy-sequence waiting to be marked
     * @param v an array containing the length and the starting position of
     * candy-sequence
     * @param isRow if true switching the row and the column indexes
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
     * All candies fall when there's empty space in their column.
     *
     * In order to work smoothly, first we transpose the basic board.
     *
     * @param l the level which contains the board
     * @return 20 * number of empty candies
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
                    return new Candy(Candy.getRandomColor());
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

    public static int processLevel(Level current) {
        int iterations = 0;
        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(current)) {
                applyGravity(current);
            } else {
                break;
            }
        }
        return iterations;
    }

    public static long[] processLevelWithState(Level current, List<String> states) {
        long iterations;
        long sum = 0;
        
        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
            if (popAllMarked(current)) {
                states.add(current.getBoardState());
                sum += applyGravity(current);
                states.add(current.getBoardState());
            } else {
                break;
            }
        }
        return new long[] {iterations, sum};
    }
    
    public static void resetLevel(Level current) {
        try {
            LevelBuilder lb = new StandardLevelBuilder();
            
            if (current instanceof StandardLevel) {
                lb = new StandardLevelBuilder(current);
            } // ...
            
            current = lb
                    .fillBoard()
                    .create();
            
            processLevel(current);
        } catch (Exception ex) {
        }
    }
}
