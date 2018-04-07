package jordan.szalontai.unicrush;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a level of a game.
 *
 * @author Szalontai Jordán
 */
@Getter
@Setter
public abstract class Level {

    /**
     * The possible messages that can be displayed if we earn a lot of points.
     */
    public static final String[] MESSAGES = {
        "Sweet",
        "Delicious",
        "Divine",
        "Tasty"
    };

    private int boardSize;
    private int availableSteps;
    private int scoreToComplete;
    private boolean transposed;
    
    /**
     * The string representation of the initial board state.
     * 
     * @return string that represents the initial board state
     */
    @Getter
    protected String initialState;

    /**
     * 2D-array that represents the coordinates of the wall positions.
     *
     * @param walls the new value to be set
     * @return the 2D-array that represents the coordinates of the wall
     * positions
     */
    protected Integer[][] walls;

    /**
     * 2D-array that represents the board of {@code Candy} instances.
     *
     * @param board the new value to be set
     * @return 2D-array that represents the board of {@code Candy} instances
     */
    protected Candy[][] board;

    /**
     * Constructs an empty level.
     */
    public Level() {
    }

    /**
     * Constructs a level with the basic fields initialized.
     *
     * @param scoreToComplete the score required to complete the level
     * @param steps the maximum steps that can be used on this level
     * @param boardSize the dimensions of the board, a {@code Level}'s board is
     * always quadratic
     * @param walls the template {@code String} representing the coordinates of
     * the walls
     */
    public Level(int scoreToComplete, int steps, int boardSize, String walls) {
        this.scoreToComplete = scoreToComplete;
        this.availableSteps = steps;
        this.boardSize = boardSize;
        this.board = new Candy[boardSize][boardSize];
        this.transposed = false;
    }

    /**
     * Returns the desired element of the board.
     *
     * @param i row index
     * @param j column index
     * @return a {@code Candy} instance that is in the board of this level,
     * {@code null} if we give incorrect row or column indexes
     */
    public Candy get(int i, int j) {
        try {
            if (transposed) {
                return board[j][i];
            } else {
                return board[i][j];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Switches the value of the {@code transposed} field.
     */
    public void transpose() {
        this.transposed = !this.transposed;
    }

    /**
     * Sets a {@code Candy} instance in the board.
     *
     * @param i row index
     * @param j column index
     * @param c the new {@code Candy} instance
     * @throws ArrayIndexOutOfBoundsException if we give incorrect row or column
     * indexes
     */
    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException {
        if (transposed) {
            board[j][i] = c;
        } else {
            board[i][j] = c;
        }
    }

    /**
     * Swaps two {@code Candy} instances on the board, if a given statement is
     * true.
     *
     * @param coors an array representing the coordinates of the {@code Candy}
     * instances in the board
     * @return {@code true} if swap was successful, {@code false} if not
     */
    public boolean swap(Integer[][] coors) {
        boolean success = testSwap(coors);

        if (!success) {
            return false;
        }

        Candy tmp = board[coors[0][0]][coors[0][1]];
        board[coors[0][0]][coors[0][1]] = board[coors[1][0]][coors[1][1]];
        board[coors[1][0]][coors[1][1]] = tmp;

        return true;
    }

    private boolean testSwap(Integer[][] coors) {
        return Math.abs(coors[0][0] - coors[1][0]) + Math.abs(coors[0][1] - coors[1][1]) == 1;
    }

    /**
     * Represents the board with a {@code String} that can be processed easily.
     *
     * <p>
     * Example usage</p>
     * <pre>
     * We have a <i>Level</i>:
     *
     *     [x, R, B, x]
     *     [G, G, B, G]
     *     [B, R, Y, G]
     *     [x, B, B, x]
     *
     * The result of <i>getBoardState()</i> is the following
     *
     *     xRBx;GGBG;BRYG;xBBx;
     * </pre>
     *
     * @return the board state {@code String} produced as mentioned above
     */
    public String getBoardState() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("\\W", "")
                .replaceAll("null", "x"))
                .reduce("", (sum, current) -> sum.concat(current).concat(";"));
    }

    /**
     * Returns a {@code String} representation of {@code this} instance.
     *
     * <pre>
     * Example:
     *
     *     [x, R, B, x]
     *     [G, G, B, G]
     *     [B, R, Y, G]
     *     [x, B, B, x]
     * </pre>
     *
     * @return the {@code String} representation of {@code this} instance as the
     * example shows above
     */
    @Override
    public String toString() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("null", "x")
                .replaceAll("E", " "))
                .reduce("", (output, currentRow) -> output += currentRow + "\n");
    }

    /**
     * Gives a random message from the defined messages.
     *
     * @return a random element of {@code MESSAGES}
     */
    public static String getMessage() {
        return MESSAGES[(int) (Math.random() * MESSAGES.length)];
    }
}
