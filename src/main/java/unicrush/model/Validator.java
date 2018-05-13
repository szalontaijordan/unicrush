package unicrush.model;

/*-
 * #%L
 * unicrush
 * %%
 * Copyright (C) 2018 Faculty of Informatics
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import unicrush.model.db.ScoreEntity;

/**
 * Class for wrapping the validation logics of the game.
 *
 * @author Szalontai Jordán
 */
public class Validator {

    //CHECKSTYLE:OFF
    private static Validator instance;

    private Validator() {
    }
    //CHECKSTYLE:ON

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
        return isTemplate(selectedCandies);
    }

    /**
     * Returns if the given iterations equal to the maximum iterations defined in the
     * {@code CandyCrushGame} class.
     *
     * @param iterations the number of iterations we compare
     * @return {@code true} if the parameter equals the defined constant, {@code false} otherwise
     */
    public boolean isMaxIterations(int iterations) {
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
     * @param str the string object we inspect
     * @return {@code true} if the string is either null or empty, {@code false} otherwise
     */
    public boolean isEmptyString(String str) {
        return str == null || str.equals("");
    }

    /**
     * Returns if the given string is a template string containing coordinates.
     *
     * <p>
     * The regular expression pattern is from StackOverflow, from <a
     * href="https://stackoverflow.com/questions/3175802/regex-colon-separated-list">this
     * article</a>
     * </p>
     *
     * @param str the string object we inspect
     * @return {@code true} if the string matches the regular expression that validates any string
     * with coordinates separated by semi-colons, {@code false} otherwise (coordinates are numbers
     * separated by commas)
     */
    public boolean isTemplate(String str) {
        return isTemplate(str, "\\s*([0-9]+,[0-9]+)\\s*(?:(?:;(?:\\s*([0-9]+,[0-9]+)+\\s*)?)+)?");
    }

    /**
     * Returns if the given string is a template string containing coordinates depending on the
     * given regular expression.
     *
     * @param str the string object we inspect
     * @param regex the regex that determines if a string is a template or not
     * @return {@code true} if the string matches the regular expression that validates it,
     * {@code false} otherwise
     */
    public boolean isTemplate(String str, String regex) {
        return !isEmptyString(str) && str.matches(regex);
    }

    /**
     * Returns if the user has reached a new high score.
     *
     * @param found the entity that was found with some score
     * @param userScore the user's score
     * @return {@code true} if the found entity is null or the user's score is greater than the
     * score we found
     */
    public boolean isNewHighScore(ScoreEntity found, int userScore) {
        return found == null || userScore > found.getScore();
    }

    //CHECKSTYLE:OFF
    private boolean isZero(int value) {
        return value == 0;
    }
    //CHECKSTYLE:ON
    
    /**
     * Returns the singleton instance of this class.
     *
     * @return the validator object
     */
    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }

        return instance;
    }
}
