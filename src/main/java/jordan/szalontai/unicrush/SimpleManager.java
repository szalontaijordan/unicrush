package jordan.szalontai.unicrush;

import java.util.ArrayList;
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
public class SimpleManager implements LevelManager {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleManager.class);
    public static SimpleManager instance;
    
    private SimpleManager() {
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
        
        logger.trace("Current level:\n{}", level.toString());
        return popHappened;
    }

    private void markAllCandies(Level l) {
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

    private void mark(Level l, int row, Integer[] v) {
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
            
            logger.trace("Candies in active column:\n{}", candies.toString());
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
        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
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

        for (iterations = 0; iterations < UniGame.MAX_ITERATION; iterations++) {
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
    
    public synchronized static SimpleManager getInstance() {
        if (instance == null) {
            instance = new SimpleManager();
        }
        
        return instance;
    }

}
