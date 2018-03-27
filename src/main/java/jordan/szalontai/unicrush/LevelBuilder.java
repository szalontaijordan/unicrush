package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 * An abstract class for building instances of {@code Level}.
 *
 * This class follows the builder pattern.
 *
 * @author Szalontai Jordán
 */
public abstract class LevelBuilder {

    protected Level level;

    /**
     * An empty constructor for the subclasses.
     */
    public LevelBuilder() {
    }

    /**
     * A constructor to set the {@code level} field of the class, so we don't
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
        level.setWalls(processCoordinateString(template));
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
        return level;
    }

    /**
     * Creating a 2D-array that has coordinates that indicate locations on a
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
