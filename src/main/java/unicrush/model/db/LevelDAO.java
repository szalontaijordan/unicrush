package unicrush.model.db;

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

import java.util.List;

/**
 * Interface describing actions that can be done with level entities.
 *
 * @author Szalontai Jordán
 */
public interface LevelDAO {

    /**
     * Returns a plain old java object that represents a level entity in a database.
     *
     * @param id the id field of the database entity
     * @return an object representing a level entity in the database
     */
    public LevelEntity findLevel(int id);

    /**
     * Returns all the level entities that can be found in the database.
     *
     * @return a list containing level entities
     */
    public List<LevelEntity> findAll();

    /**
     * Creates a new level entity in the database with fields based on the given parameters.
     *
     * @param id the id of the level
     * @param size the size of the board
     * @param walls the template string that represents coordinates where walls should be
     * @param score the score required to complete this level
     * @param steps the amount of steps required to complete this level
     */
    public void create(int id, int size, String walls, int score, int steps);
}
