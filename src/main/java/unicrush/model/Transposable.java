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
 * Interface for an object that can be transposed.
 *
 * @author Szalontai Jordán
 */
public interface Transposable {

    /**
     * Transposes the object.
     */
    public void transpose();

    /**
     * Returns the desired element of the board.
     *
     * @param i row index
     * @param j column index
     * @return a {@code Candy} instance that is in the board of this level,
     * {@code null} if we give incorrect row or column indexes
     */
    public Candy get(int i, int j);

    /**
     * Sets a {@code Candy} instance in the board.
     *
     * @param i row index
     * @param j column index
     * @param c the new {@code Candy} instance
     * @throws ArrayIndexOutOfBoundsException if we give incorrect row or column
     * indexes
     */
    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException;
}
