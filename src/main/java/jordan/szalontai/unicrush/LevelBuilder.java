package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 * Abstract class for building instances of {@code Level}.
 *
 * This class follows the builder pattern.
 *
 * @author Szalontai Jordán
 */
public abstract class LevelBuilder {

    /**
     * The {@code Level} instance we build.
     */
    protected Level level;

    /**
     * Constructs an empty instance.
     */
    public LevelBuilder() {
    }

    /**
     * Constructor to set the {@code level} field of the class, so we don't
     * start from the beginnings.
     *
     * @param level the level that we would like to continue building
     */
    public LevelBuilder(Level level) {
        this.level = level;
    }

    /**
     * Sets the {@code boardSize} filed of the {@code level} we are building.
     *
     * @param size the new size for the {@code level} we are building
     * @return {@code this} so we can chain the methods after each other
     * @throws Exception the new size must be at least two
     */
    public LevelBuilder setBoardSize(int size) throws Exception {
        if (size < 2) {
            throw new Exception("Too small board!");
        }
        level.setBoardSize(size);
        return this;
    }

    /**
     * Sets the {@code availableSteps} filed of the {@code level} we are
     * building.
     *
     * @param steps the new amount of steps for the {@code level} we are
     * building
     * @return {@code this} so we can chain the methods after each other
     * @throws Exception the amount of steps cannot be negative or zero
     */
    public LevelBuilder setAvailableSteps(int steps) throws Exception {
        if (steps < 1) {
            throw new Exception("Steps must be positive");
        }
        level.setAvailableSteps(steps);
        return this;
    }

    /**
     * Sets the {@code availableSteps} filed of the {@code level} we are
     * building.
     *
     * @param score the score required to complete the {@code level} we are
     * building
     * @return {@code this} so we can chain the methods after each other
     * @throws Exception the score required to complete a level cannot be
     * negative or zero
     */
    public LevelBuilder setCompliteScore(int score) throws Exception {
        if (score < 1) {
            throw new Exception("Steps must be positive");
        }
        level.setScoreToComplete(score);
        return this;
    }

    /**
     * Sets elements of a {@code Level}'s board to null based on a template
     * {@code String}.
     *
     * @param template a string representing the coordinates needed to be
     * replaced in the following form: {@code "i1,j1;i2,j2; ... ;in,jn"}
     * @return {@code this} so we can chain the methods after each other
     */
    public LevelBuilder putWallsFromString(String template) {
        level.walls = processCoordinateString(template);
        return this;
    }

    /**
     * Sets the elements of a {@code Level}'s board as described in the given
     * {@code String}
     *
     * @param boardState a string representing a {@code Level}' board.
     * @return {@code this} so we can chain the methods after each other
     * @throws IllegalArgumentException when the {@code boardSize}, or the
     * {@code walls} field is not set
     *
     * @see Level#getBoardState()
     */
    public LevelBuilder setBoardFromState(String boardState) throws IllegalArgumentException {
        if (level.getBoardSize() == 0) {
            throw new IllegalArgumentException("The size of the board is not set yet!");
        }

        if (level.getWalls() == null) {
            throw new IllegalArgumentException("The walls must be set, before altering the board!");
        }

        Candy[][] newBoard = new Candy[level.getBoardSize()][level.getBoardSize()];
        String[] state = boardState.split(";");

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

        level.board = newBoard;
        return this;
    }

    /**
     * An abstract method for filling up the specific instances as desired.
     *
     * @return {@code this}, so we can chain the methods after each other
     * @throws Exception if something unexpected happens, e.g. the walls are not
     * set
     */
    public abstract LevelBuilder fillBoard() throws Exception;

    /**
     * Gives back the level we were building.
     *
     * This is a terminal step, so we cannot chain methods anymore after this.
     *
     * @return the level instance of this builder
     */
    public Level create() {
        if (level.initialState == null || level.initialState.equals("")) {
            level.initialState = level.getBoardState();
        }
        return level;
    }

    /**
     * Creates a 2D-array that has coordinates that indicate locations on a
     * board.
     *
     * @param walls A String with the pattern. An example String: "0,0;0,1;0,2".
     * @return the 2D-array containing the coordinates
     */
    public static Integer[][] processCoordinateString(String walls) {
        return Arrays.stream(walls.split(";"))
                .map(coor -> Arrays.stream(coor.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
