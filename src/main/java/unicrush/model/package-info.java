/**
 * Package containing the classes, that define the logic of the game.
 *
 * <p>
 * The basic concept of the UniCrush game, is that you can play the game on levels. These levels are
 * responsible for keeping track of the board. On the board you have candies you can swap. These
 * operations on a level such as swapping two candy instances are done by a manager.</p>
 *
 * <p>
 * The levels of a game are loaded from an oracle database.</p>
 *
 * <p>
 * It is important to note, that the board of level instance is always a quadratic (N x N) matrix,
 * and it can be transposed in order to handle the rows and the columns easily. If you would like to
 * play on a level, but have an empty space somewhere put {@code null} on its coordinates.</p>
 *
 * <p>
 * This package also contains a validator singleton class that has methods with {@code boolean}
 * return value. This class is used in most of the classes to validate values.</p>
 */
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
