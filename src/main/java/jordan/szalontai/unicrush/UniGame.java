package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.List;

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
     * @see LevelManager#processLevel
     * @see LevelManager#resetLevel
     */
    public void startLevel(int levelIndex) {
        Level level = levels.get(levelIndex);
        
        System.out.println("Preprocessing");

        int iterations = LevelManager.processLevel(level);

        if (iterations == MAX_ITERATION) {
            System.out.println("Maxit, preprocessing again");
            LevelManager.resetLevel(level);
            LevelManager.processLevel(level);
        }

        System.out.println("Start:");
        System.out.println(level);

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
    public int getCurrentLevel() {
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
        Level l = new StandardLevelBuilder()
                .setCompliteScore(500)
                .setAvailableSteps(20)
                .setBoardSize(8)
                .putWallsFromString("0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7")
                .fillBoard()
                .create();

        levels.add(l);
    }

    @Override
    public String toString() {
        return "gg";
    }
}
