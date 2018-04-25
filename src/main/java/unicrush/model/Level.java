package unicrush.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing a level of a game.
 *
 * @author Szalontai Jordán
 */
public final class Level implements Transposable {

    /**
     * The possible messages that can be displayed if we earn a lot of points.
     */
    public static final String[] MESSAGES = {
        "Sweet",
        "Delicious",
        "Divine",
        "Tasty"
    };

    /**
     * Enum for the level types.
     *
     * @author Szalontai Jordán
     */
    public enum Type {

        STANDARD, JELLY, INGREDIENT
    }

    private final int boardSize;
    private final int scoreToComplete;

    private final Type type;
    private final String initialState;
    private final Integer[][] walls;
    private final Candy[][] board;

    private int availableSteps;
    private boolean transposed;

    /**
     * Constructs a level with the basic fields initialized.
     *
     * @param scoreToComplete the score required to complete the level
     * @param steps the maximum steps that can be used on this level
     * @param boardSize the dimensions of the board, a {@code Level}'s board is always quadratic
     * @param walls the template {@code String} representing the coordinates of the walls
     */
    private Level(Builder builder) {
        this.boardSize = builder.boardSize;
        this.scoreToComplete = builder.scoreToComplete;
        this.type = builder.type;
        this.initialState = builder.initialState;
        this.walls = builder.walls;
        this.board = builder.board;
        this.availableSteps = builder.availableSteps;
        this.transposed = builder.transposed;
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
     * The result of <i>getBoardState()</i> is the following if not transposed:
     *
     *     xRBx;GGBG;BRYG;xBBx
     *
     * Othervise:
     *
     *     xGBx;RGRB;BBYB;xGGx
     *
     * </pre>
     *
     * @return the board state {@code String} produced as mentioned above
     */
    public String getBoardState() {
        String state = "";

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                state += get(i, j) == null ? "x" : get(i, j).toString();
            }
            state += ";";
        }
        return state.substring(0, state.length() - 1);
    }

    /**
     * Swaps two {@code Candy} instances on the board, if a given statement is true.
     *
     * @param coors an array representing the coordinates of the {@code Candy} instances in the
     * board
     * @return {@code true} if swap was successful, {@code false} if not
     */
    public boolean swap(Integer[][] coors) {
        boolean success = testSwap(coors);

        if (!success) {
            return false;
        }

        if (board[coors[0][0]][coors[0][1]] == null || board[coors[1][0]][coors[1][1]] == null) {
            return false;
        }

        Candy tmp = board[coors[0][0]][coors[0][1]];
        board[coors[0][0]][coors[0][1]] = board[coors[1][0]][coors[1][1]];
        board[coors[1][0]][coors[1][1]] = tmp;

        return true;
    }

    @Override
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

    @Override
    public void transpose() {
        this.transposed = !this.transposed;
    }

    @Override
    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException {
        if (transposed) {
            board[j][i] = c;
        } else {
            board[i][j] = c;
        }
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
     * @return the {@code String} representation of {@code this} instance as the example shows above
     */
    @Override
    public String toString() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("null", "x")
                .replaceAll("E", " ")
                )
                .collect(Collectors.joining("\n"));
    }

    /**
     * Gives a random message from the defined messages.
     *
     * @return a random element of {@code MESSAGES}
     */
    public static String getMessage() {
        return MESSAGES[(int) (Math.random() * MESSAGES.length)];
    }

    /**
     * Returns a 2D-array that represents coordinates.
     *
     * <p>
     * The idea of this, is that in the template we list some coordinates separated, and we parse
     * this template string.</p>
     * <p>
     * For example {@code Level.Builder.createCoordinates("1,2;3,4;5,6");} returns
     * </p>
     *
     * <pre>
     *     new Integer[][]{ { 1, 2 }, { 3, 4 }, { 5, 6 } }
     * </pre>
     *
     *
     * @param template a string that represents coordinates like in the example above
     * @return a 2D-array that represents coordinates
     */
    public static Integer[][] createCoordinates(String template) {
        return Arrays.stream(template.split(";"))
                .map(coor -> Arrays.stream(coor.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }

    public static String[] getMESSAGES() {
        return MESSAGES;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getScoreToComplete() {
        return scoreToComplete;
    }

    public Type getType() {
        return type;
    }

    public String getInitialState() {
        return initialState;
    }

    public Integer[][] getWalls() {
        return walls;
    }

    public Candy[][] getBoard() {
        return board;
    }

    public int getAvailableSteps() {
        return availableSteps;
    }

    public boolean isTransposed() {
        return transposed;
    }

    /**
     * Static class for the level that follows the builder pattern.
     */
    public static class Builder {

        private final Type type;
        private final int boardSize;

        private int scoreToComplete;
        private String initialState;
        private Integer[][] walls;
        private Candy[][] board;

        private int availableSteps;
        private boolean transposed;

        /**
         * Constructs a builder for a level, that will represent some pieces of essential
         * information about the level.
         *
         * @param type the type of the level
         * @param boardSize the size of the level (all levels are quadratic)
         * @throws IllegalArgumentException if the {@code type} is {@code null} or the
         * {@code boardSize} is less than 2
         */
        public Builder(Type type, int boardSize) throws IllegalArgumentException {
            if (type == null || boardSize < 2) {
                throw new IllegalArgumentException("Invalid level type, or too small board!");
            }
            this.type = type;
            this.boardSize = boardSize;
            this.transposed = false;
        }

        /**
         * Constructs a builder instance, which fields will be based on an existing level object.
         *
         * @param src the level object as a starting point
         */
        public Builder(Level src) {
            this.type = src.type;
            this.boardSize = src.boardSize;
            this.transposed = src.transposed;
            this.scoreToComplete = src.scoreToComplete;
            this.availableSteps = src.availableSteps;
            this.board = src.board.clone();
            this.walls = src.walls.clone();
            this.initialState = src.initialState;
        }

        /**
         * Sets the score that is required to complete this level.
         *
         * @param score the new score
         * @return {@code this} so we can chain builder methods
         */
        public Builder withCompleteScore(final int score) {
            this.scoreToComplete = score;
            return this;
        }

        /**
         * Sets the amount of steps that are allowed on this level.
         *
         * @param steps the steps that are available
         * @return {@code this} so we can chain builder methods
         */
        public Builder withAvailableSteps(final int steps) {
            this.availableSteps = steps;
            return this;
        }

        /**
         * Sets the walls array.
         *
         * @param walls the walls array
         * @return {@code this} so we can chain builder methods
         */
        public Builder putWalls(final Integer[][] walls) {
            this.walls = walls.clone();
            return this;
        }

        /**
         * Sets the board 2D-array with random {@code Candy} instances.
         *
         * @return {@code this} so we can chain builder methods
         */
        public Builder fillBoard() {
            this.board = fillUpRandom();
            return this;
        }

        /**
         * Sets the board 2D-array with {@code Candy} instances specified in a template string.
         *
         * @param template the template string representing information about the new board
         * @return {@code this} so we can chain builder methods
         */
        public Builder fillBoard(String template) {
            this.board = fillUpFromState(template);
            return this;
        }

        /**
         * Builds a new {@code Level} object.
         *
         * <p>
         * This method also sets the {@code initialState} for the level, so we can refer to it in
         * the future.</p>
         *
         * @return a {@code Level} object with the builded fields
         */
        public Level build() {
            if (this.initialState == null || this.initialState.equals("")) {
                this.initialState = setupInitialState();
            }
            return new Level(this);
        }

        private String setupInitialState() {
            return Arrays.stream(board)
                    .map(row -> Arrays.toString(row)
                    .replaceAll("\\W", "")
                    .replaceAll("null", "x")
                    )
                    .collect(Collectors.joining(";"));
        }

        private Candy[][] fillUpRandom() throws IllegalArgumentException {
            if (walls == null) {
                throw new IllegalArgumentException("Walls must be set first!");
            }

            Candy[][] newBoard = new Candy[boardSize][boardSize];

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    newBoard[i][j] = new Candy(Candy.getRandomColorState());
                }
            }

            Arrays.stream(walls)
                    .forEach(wall -> newBoard[wall[0]][wall[1]] = null);

            return newBoard;
        }

        private Candy[][] fillUpFromState(String template) throws IllegalArgumentException {
            if (walls == null) {
                throw new IllegalArgumentException("The walls must be set, before altering the board!");
            }

            Candy[][] newBoard = new Candy[boardSize][boardSize];
            String[] state = template.split(";");

            if (state.length != state[0].length()) {
                throw new IllegalArgumentException("Malformed board state string");
            }

            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[i].length(); j++) {
                    if (state[i].charAt(j) == 'x') {
                        newBoard[i][j] = null;
                    } else {
                        newBoard[i][j] = new Candy(Candy.getStateFromChar(state[i].charAt(j)));
                    }
                }
            }
            return newBoard;
        }
    }
}