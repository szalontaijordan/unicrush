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
 * The possible states of a {@code Candy}.
 *
 * @author Szalontai Jordán
 */
public enum CandyState {

    /**
     * A state that represents an empty cell on the board.
     */
    EMPTY,
    /**
     * A state that represents a red candy.
     */
    RED,
    /**
     * A state that represents a green candy.
     */
    GREEN,
    /**
     * A state that represents a blue candy.
     */
    BLUE,
    /**
     * A state that represents an orange candy.
     */
    ORANGE,
    /**
     * A state that represents a purple candy.
     */
    PRUPLE,
    /**
     * A state that represents a yellow candy.
     */
    YELLOW
}
