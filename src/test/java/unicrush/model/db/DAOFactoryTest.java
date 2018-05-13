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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@code unicrush.model.db.DAOFactory}.
 *
 * @author Szalontai Jordán
 */
public class DAOFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOFactoryTest.class);

    public DAOFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class DAOFactory");
    }

    /**
     * Test of getInstance method, of class DAOFactory.
     */
    @Test
    public void testGetInstance() {
        DAOFactory factory1 = DAOFactory.getInstance();
        DAOFactory factory2 = DAOFactory.getInstance();

        Assert.assertTrue(factory1.equals(factory2));
    }

    /**
     * Test of createLevelDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateLevelDAO() {
        LevelDAO levelDao = DAOFactory.getInstance().createLevelDAO();
        Assert.assertTrue(levelDao instanceof LevelDAOSimple);
    }

    /**
     * Test of createUserDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateUserDAO() {
        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        Assert.assertTrue(userDao instanceof UserDAOSimple);
    }

    /**
     * Test of createScoreDAO method, of class DAOFactory.
     */
    @Test
    public void testCreateScoreDAO() {
        ScoreDAO scoreDao = DAOFactory.getInstance().createScoreDAO();
        Assert.assertTrue(scoreDao instanceof ScoreDAOSimple);
    }
}
