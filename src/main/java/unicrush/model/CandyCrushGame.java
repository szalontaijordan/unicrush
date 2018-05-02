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

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class representing the game like Candy Crush with a list of levels you can
 * play.
 *
 * @author Szalontai Jordán
 */
public class CandyCrushGame implements Game {

    /**
     * The maximum amount of allowed iterations while processing a {@code Level}
     * of the {@code CandyCrushGame}.
     */
    public static final int MAX_ITERATION = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger(CandyCrushGame.class);

    private long playerScore;
    private List<Level> levels;
    private int currentLevelIndex;

    /**
     * Constructs an object that wraps its levels and information about the
     * player playing the game.
     * 
     */
    public CandyCrushGame() {
        this.playerScore = 0;
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void setPlayerScore(long playerScore) {
        this.playerScore = playerScore;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setCurrentLevelIndex(int currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }

    @Override
    public void startLevel(Level level) {
        LOGGER.debug("Reseting player score");
        this.playerScore = 0;
        this.currentLevelIndex = levels.indexOf(level);
        LOGGER.info("Started level:\n{}", level.toString());
    }

    @Override
    public void initLevels() throws Exception {
        // creating level, we didn't set up any DB connection yet
        String template = "0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7";
                
        Level l = new Level.Builder(Level.Type.STANDARD, 8)
                .withCompleteScore(10000)
                .withAvailableSteps(20)
                .putWalls(Level.createCoordinates(template))
                .fillBoard()
                .build();

        levels.add(l);
    }
    
    @Override
    public Level getLevel(int index) {
        return levels.get(index);
    }

    @Override
    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    @Override
    public long getPlayerScore() {
        return playerScore;
    }      

    @Override
    public void addToScore(long plus) {
        this.playerScore += plus;
    }

    @Override
    public String toString() {
        return "CandyCrushGame{" + "playerScore=" + playerScore + ", levels=" + levels + ", currentLevelIndex=" + currentLevelIndex + '}';
    }
}
