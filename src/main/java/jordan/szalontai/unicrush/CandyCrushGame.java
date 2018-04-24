package jordan.szalontai.unicrush;

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
        
        LOGGER.info("Preprocessing current level ...");
        int iterations = SimpleLevelManager.getInstance().process(level);

        if (iterations == MAX_ITERATION) {
            LOGGER.info("Maximum iteration, reseting level ...");
            LevelManager.reset(level);
            SimpleLevelManager.getInstance().process(level);
        }

        System.out.println("Start:");
        LOGGER.info("Started level:\n{}", level.toString());
    }

    @Override
    public void initLevels() throws Exception {
        // creating level, we didn't set up any DB connection yet
        Level l = new Level.Builder(LevelType.STANDARD, 8)
                .withCompleteScore(500)
                .withAvailableSteps(20)
                .putWalls("0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7")
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
