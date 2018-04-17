package jordan.szalontai.unicrush;

import java.util.Arrays;
import java.util.List;

/**
 * Interface representing actions that can be done on a level.
 *
 * @author Szalontai Jordán
 */
public interface LevelManager {

    /**
     * Iterations processing the level and returning the score.
     *
     * <p>
     * Iterations should consist of popping the matching {@code Candy} instances
     * and then applying the gravity logic to the {@code Level}'s board.
     *
     * <p>
     * We check how many iterations occurred, so this cannot be an infinite
     * loop. This method might change the model of the {@code Level} given as a
     * parameter.</p>
     *
     * @param level the {@code Level} on which we do these iterations
     * @return how many iterations occurred
     */
    public int process(Level level);

    /**
     * Iterations processing the level and returning information in a list.
     *
     * <p>
     * Iterations should consist of popping the matching {@code Candy} instances
     * and then applying the gravity logic to the {@code Level}'s board.
     * </p>
     * <p>
     * We should check how many iterations occurred, so this cannot be an
     * infinite loop and in addition we summarize the points that
     * {@code applyGravity} returns. It is important to be aware of the fact,
     * that this method might change model of the {@code Level} given as a
     * parameter AND add the given {@code Level}'s {@code boardState String} to
     * the returned {@code List<String>}.</p>
     *
     * @param level the {@code Level} on which we do these iterations
     * @return a list starting with information about the iteration and the rest
     * are the board states of each iteration
     */
    public List<String> processWithState(Level level);

    /**
     * Resetting the {@code board} based on the original board state.
     *
     * @param level the {@code Level} instance to reset
     */
    public static void reset(Level level) {
        level = new Level.Builder(level)
                .fillBoard(level.getInitialState())
                .build();
    }

    /**
     * Returns a 2D-array that represents coordinates.
     *
     * <p>
     * The idea of this, is that in the template we list some coordinates
     * separated, and we parse this template string.</p>
     * <p>
     * For example {@code Level.Builder.processCoordinateString("1,2;3,4;5,6");}
     * returns
     * </p>
     *
     * <pre>
     *     new Integer[][]{ { 1, 2 }, { 3, 4 }, { 5, 6 } }
     * </pre>
     *
     *
     * @param template a string that represents coordinates like in the example
     * above
     * @return a 2D-array that represents coordinates
     */
    public static Integer[][] processCoordinateString(String template) {
        return Arrays.stream(template.split(";"))
                .map(coor -> Arrays.stream(coor.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
