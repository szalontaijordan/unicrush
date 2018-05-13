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
 * Interface describing actions that can be done with score entities.
 *
 * @author Szalontai Jordán
 */
public interface ScoreDAO {

    /**
     * Creates a new score entity in the database with fields based on the given parameters.
     *
     * @param userId the id of the user
     * @param levelId the id of the level
     * @param score the score of the user on the level they played
     */
    public void create(int userId, int levelId, int score);

    /**
     * Updates the numerical score field of score entity in the database with fields based on the
     * level id and the user id.
     *
     * @param userId the id of the user
     * @param levelId the id of the level
     * @param score the score of the user on the level they played
     */
    public void update(int userId, int levelId, int score);

    /**
     * Returns a plain old java object that represents a score entity in the database, identified by
     * two fields.
     *
     * @param userId the id of the user (first component of the entity's primary key)
     * @param levelId the id of the level (second component of the entity's primary key)
     * @return an object representing a score entity in the database
     */
    public ScoreEntity find(int userId, int levelId);

    /**
     * Returns all the score entities that can be found in the database.
     *
     * @return a list containing score entities
     */
    public List<ScoreEntity> findAll();
}
