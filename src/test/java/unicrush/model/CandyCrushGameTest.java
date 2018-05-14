/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.db.DAOFactory;

/**
 * Test class for {@code unicrush.model.CandyCrushGame}.
 *
 * @author Szalontai Jordán
 */
public class CandyCrushGameTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandyCrushGameTest.class);

    private CandyCrushGame game;

    public CandyCrushGameTest() {
        this.game = new CandyCrushGame();
    }

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class CandyCrushGame");
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            DAOFactory.getInstance().close();
        } catch (Exception ex) {
        }
    }

    /**
     * Test of initLevels method, of class CandyCrushGame.
     */
    @Test
    public void testInitLevels() {
        String template;

        template = "0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7";
        Level testLevel1 = new Level.Builder(100, 8)
                .withAvailableSteps(20)
                .withCompleteScore(5000)
                .putWalls(Level.createCoordinates(template))
                .fillBoard()
                .build();

        template = "1,1;1,2;1,3;2,1;2,2;2,3;3,1;3,2;3,3";
        Level testLevel2 = new Level.Builder(102, 5)
                .withAvailableSteps(15)
                .withCompleteScore(1000)
                .putWalls(Level.createCoordinates(template))
                .fillBoard()
                .build();

        try {
            CandyCrushGame testGame = new CandyCrushGame();
            testGame.initLevels();

            testGame.getLevels().stream()
                    .filter(level -> level.getID() == 100)
                    .forEach(level -> Assert.assertTrue(level.equals(testLevel1)));

            testGame.getLevels().stream()
                    .filter(level -> level.getID() == 102)
                    .forEach(level -> Assert.assertTrue(level.equals(testLevel2)));
        } catch (Exception ex) {
        }
    }

}
