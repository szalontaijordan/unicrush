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
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.db.LevelDAO;
import unicrush.model.db.LevelDAOFactory;
import unicrush.model.db.LevelPOJO;

/**
 * Class representing the game with a list of levels you can play with the help of the defined
 * manager.
 *
 * @author Szalontai Jordán
 */
public class CandyCrushGame implements Game {

    /**
     * The maximum amount of allowed iterations while processing a {@code Level} of the
     * {@code CandyCrushGame}.
     */
    public static final int MAX_ITERATION = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger(CandyCrushGame.class);

    private int playerScore;
    private int currentLevelIndex;

    private List<Level> levels;
    private LevelManager manager;

    /**
     * Constructs an object that wraps its levels and information about the player playing the game.
     *
     */
    public CandyCrushGame() {
        this.playerScore = 0;
        this.levels = new ArrayList<>();
        this.currentLevelIndex = 0;
        this.manager = new LevelManager();
    }

    @Override
    public void startLevel(Level level) {
        LOGGER.debug("Reseting player score");
        this.playerScore = 0;
        this.currentLevelIndex = levels.indexOf(level);
        this.manager.setLevel(level);
        LOGGER.info("Started level:\n{}", level.toString());
    }

    @Override
    public void initLevels() throws Exception {
        if (levels != null && !levels.isEmpty()) {
            levels.clear();
            LOGGER.warn("Clearing level list");
        }
        LOGGER.info("Creating DAO for levels");
        LevelDAO levelDao = LevelDAOFactory.getInstance().createLevelDAO();

        LOGGER.info("Creating new test level");
        //levelDao.create(100, 8, "0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7", 5000, 20);

        LOGGER.info("Fetching levels");
        levels = levelDao.findAll().stream()
                .map(pojo -> buildLevelFromPojo(pojo))
                .collect(Collectors.toList());
    }

    /**
     * Builds a {@code Level} instance from a plain old java object representing a level entity in
     * the database.
     *
     * @param pojo the entity that represents a level in the database
     * @return a {@code Level} instance based on the entity
     */
    public Level buildLevelFromPojo(LevelPOJO pojo) {
        return new Level.Builder(pojo.getLevelId(), Level.Type.STANDARD, pojo.getBoardSize())
                .withCompleteScore(pojo.getScoreToComplete())
                .withAvailableSteps(pojo.getAvailableSteps())
                .putWalls(Level.createCoordinates(pojo.getWalls()))
                .fillBoard()
                .build();
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
    public int getPlayerScore() {
        return playerScore;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setCurrentLevelIndex(int currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }

    public LevelManager getManager() {
        return manager;
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
