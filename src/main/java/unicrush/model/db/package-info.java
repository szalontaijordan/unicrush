/**
 * Package containing classes that communicate with the database.
 *
 * <p>
 * Most of these classes are data access objects and entity classes.</p>
 *
 * <p>
 * For example you can create a new {@link LevelEntity} with the following code:</p>
 * 
 * <pre>
 *  LevelDAO levelDao = DAOFactory.createLevelDAO();
 *  levelDao.create(109, 5, null, 1000, 20);
 * </pre>
 *
 * <p>
 * The scheme of the database is the following:</p>
 * <pre>
 *  UC_LEVEL(id:NUMBER, boardSize:NUMBER, walls:VARCHAR2, score:NUMBER, steps:NUMBER)
 *  UC_USER(id:NUMBER, username:VARCHAR2)
 *  UC_GAME(levelId:NUMBER, userId:NUMBER, score:VARCHAR2)
 * </pre>
 *
 */
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
