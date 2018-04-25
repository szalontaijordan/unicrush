package unicrush.controller;

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

import unicrush.model.Level;
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
}
