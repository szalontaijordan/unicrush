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

/**
 * Interface describing the basic behaviors of a {@code Game}.
 *
 * @author Szalontai Jordán
 */
public interface Game {

    /**
     * Initializing the levels of the game with a basic structure, that can be
     * modified if necessary.
     *
     * Usually you might use this method to connect to a database and retrieve
     * some data.
     *
     * @throws Exception if something bad happens during the initializing
     * process e.g. I/O errors
     */
    public void initLevels() throws Exception;

    /**
     * Returns a concrete {@code Level} instance of the game-
     *
     * @param index the index of a level we need
     * @return a {@code Level} instance representing the level we wanted to get
     */
    public Level getLevel(int index);

    /**
     * Returns the level on which the game is happening.
     *
     * @return the {@code Level} instance with the current index
     */
    public Level getCurrentLevel();
    
    /**
     * Initializes a {@code Level} so there are not any matching {@code Candy}
     * sequences.
     *
     * @param level the {@code Level} we'd like to start
     *
     * @see unicrush.controller.SimpleLevelManager#process
     * @see unicrush.controller.SimpleLevelManager#reset
     */
    public void startLevel(Level level);
    
    /**
     * The way of starting the defined level of the game on which the actual
     * game is happening.
     */
    public default void startCurrentLevel() {
        startLevel(getCurrentLevel());
    }
    
    /**
     * Returns information about the player's current score.
     *
     * @return the current score of a player
     */
    public long getPlayerScore();
    
    /**
     * Used for modifying the score of a player.
     *
     * @param plus the amount of points that should be added to some value
     * representing the player's score
     */
    public void addToScore(long plus);
}
