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
public class UniGame implements Game {

    /**
     * The maximum amount of iterations that can occur during processing a
     * {@code Level} of the {@code UniGame}.
     */
    public static final int MAX_ITERATION = 50;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UniGame.class);

    private long playerScore;
    private int currentLevel;
    private List<Level> levels;

    /**
     * Constructs a {@code Game} and setting its current level to the given
     * parameter.
     *
     * @param currentLevel an index of the {@code Level}
     */
    public UniGame(int currentLevel) {
        this.playerScore = 0;
        this.levels = new ArrayList<>();
        this.currentLevel = currentLevel;
    }

    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Initializes a {@code Level} so there are not any matching {@code Candy}
     * sequences.
     *
     * @param levelIndex the index of the {@code Level} we'd like to start
     *
     * @see SimpleManager.getInstance()#process
     * @see SimpleManager.getInstance()#reset
     */
    public void startLevel(int levelIndex) {
        Level level = levels.get(levelIndex);
        
        LOGGER.info("Preprocessing current level ...");
        

        int iterations = SimpleManager.getInstance().process(level);

        if (iterations == MAX_ITERATION) {
            LOGGER.info("Maximum iteration, reseting level ...");
            LevelManager.reset(level);
            SimpleManager.getInstance().process(level);
        }

        System.out.println("Start:");
        LOGGER.info("Started level:\n{}", level.toString());

        this.playerScore = 0;
    }

    public void setCurrentLevel(int newCurrent) {
        this.currentLevel = newCurrent;
    }

    @Override
    public Level getLevel(int index) {
        return levels.get(index);
    }

    @Override
    public long getPlayerScore() {
        return playerScore;
    }

    @Override
    public int getCurrent() {
        return currentLevel;
    }

    @Override
    public void startCurrentLevel() {
        startLevel(currentLevel);
    }

    @Override
    public void addToScore(long plus) {
        this.playerScore += plus;
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
    public String toString() {
        return "gg";
    }
}
