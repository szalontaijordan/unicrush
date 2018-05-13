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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class for creating data access objects for a level entity.
 *
 * @author Szalontai Jordán
 */
public class DAOFactory implements AutoCloseable {

    //CHECKSTYLE:OFF
    private static DAOFactory instance;

    private static EntityManager entityManager;
    private static EntityManagerFactory factory;
    
    static {
        instance = new DAOFactory();
        factory = Persistence.createEntityManagerFactory("UNICRUSH");
        entityManager = factory.createEntityManager();
    }
    
    private DAOFactory() {
    }
    //CHECKSTYLE:ON

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static DAOFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new data access object for level entities.
     *
     * @return an object that defines actions for level entities
     */
    public LevelDAO createLevelDAO() {
        return new LevelDAOSimple(entityManager);
    }

    /**
     * Creates a new data access object for user entities.
     *
     * @return an object that defines actions for user entities
     */
    public UserDAO createUserDAO() {
        return new UserDAOSimple(entityManager);
    }

    /**
     * Creates a new data access object for score entities.
     *
     * @return an object that defines actions for score entities
     */
    public ScoreDAO createScoreDAO() {
        return new ScoreDAOSimple(entityManager);
    }

    /**
     * Closes the {@code EntityManager} and the {@code EntityManagerFactory} objects.
     *
     * @throws Exception if an error occurs during the closing process
     */
    @Override
    public void close() throws Exception {
        entityManager.close();
        factory.close();
    }
}
