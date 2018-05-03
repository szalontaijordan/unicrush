package unicrush.model;

/**
 * Class for wrapping the validation logics of the game.
 *
 * @author Szalontai Jordán
 */
public class Validator {

    private int checks;

    /**
     * Constructs an object that is responsible for doing validation checks during the game.
     */
    public Validator() {
        this.checks = 0;
    }

    /**
     * Returns if the given game is in an end-game situation.
     *
     * <p>
     * A {@code Game} is in an end-game situation if the player reached the maximum score or there
     * are no more available steps.</p>
     *
     * @param game the game object we inspect
     * @param steps the string representation of the remaining steps
     * @return {@code true} if either the player reached the maximum score or the given steps are
     * zero, {@code false} otherwise
     */
    public boolean isEndGameSituation(Game game, int steps) {
        checks++;
        return isMaxScore(game) || isNoMoreSteps(steps);
    }

    /**
     * Returns if the player of the game has reached the maximum score of the current level.
     *
     * @param game the game object we inspect
     * @return {@code true} if the player's score is greater or equal to the score needed to
     * complete the current level, {@code false} otherwise
     */
    public boolean isMaxScore(Game game) {
        return game.getPlayerScore() >= game.getCurrentLevel().getScoreToComplete();
    }

    /**
     * Returns if the gives steps are zero.
     *
     * @param steps the available steps
     * @return {@code true} if the given parameter is equal to zero, {@code false} otherwise
     */
    public boolean isNoMoreSteps(int steps) {
        return isZero(steps);
    }

    /**
     * Returns if both the described coordinates in the given template string are defined.
     *
     * @param selectedCandies the template string of two coordinates
     * @return {@code true} if there is not any "null" substring in this template, {@code false}
     * otherwise
     */
    public boolean isTwoSelected(String selectedCandies) {
        checks++;
        return !selectedCandies.contains("null");
    }

    /**
     * Returns if the given iterations equal to the maximum iterations defined in the
     * {@code CandyCrushGame} class.
     *
     * @param iterations the number of iterations we compare
     * @return {@code true} if the parameter equals the defined constant, {@code false} otherwise
     */
    public boolean isMaxIterations(int iterations) {
        checks++;
        return iterations == CandyCrushGame.MAX_ITERATION;
    }

    /**
     * Returns if the value of the given iterations is zero.
     *
     * @param iterations the number of iterations we compare
     * @return {@code true} if the parameter equals zero, {@code false} otherwise
     */
    public boolean isNoIterations(int iterations) {
        return isZero(iterations);
    }

    /**
     * Returns if the given string is either null or empty.
     *
     * @param string the string object we inspect
     * @return {@code true} if the string is either null or empty, {@code false} otherwise
     */
    public boolean isEmptyString(String string) {
        return string == null || string.equals("");
    }

    private boolean isZero(int value) {
        checks++;
        return value == 0;
    }

    public int getChecks() {
        return checks;
    }
}
