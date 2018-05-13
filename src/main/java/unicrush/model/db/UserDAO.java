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
 * Interface describing actions that can be done with user entities.
 *
 * @author Szalontai Jordán
 */
public interface UserDAO {

    /**
     * Creates a new user entity in the database with fields based on the parameters.
     *
     * @param username the name of the new user
     */
    public void create(String username);

    /**
     * Returns a plain old java object that represents an user entity in a database.
     *
     * @param id the id field of the entity
     * @return an object representing an user entity in the database
     */
    public UserEntity get(int id);

    /**
     * Returns all user entities from the database, that have the same name as the given parameter.
     *
     * @param username the name we look for
     * @return a list containing objects representing user entities in the database
     */
    public List<UserEntity> findByName(String username);
}
