package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.List;

public class UniGame implements Game {

    /**
     * The maximum amount of iterations that can occur during processing a
     * {@code Level} of the {@code UniGame}
     */
    public static final int MAX_ITERATION = 50;

    private long playerScore;
    private int currentLevel;
    private List<Level> levels;

    /**
     * Constructing a {@code Game} and setting its current level to the given
     * parameter.
     *
     * @param currentLevel an index of the {@code Level}
     */
    public UniGame(int currentLevel) {
        this.playerScore = 0;
        this.levels = new ArrayList<>();
        this.currentLevel = currentLevel;
    }

    public Level getLevel(int index) {
        return levels.get(index);
    }

    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Initializing a {@code Level} so there are not any matching {@code Candy}
     * sequences.
     *
     * @param level the {@code Level} needed to be initialized
     * @throws Exception if anything bad happens during the initialization
     *
     * @see {@code LevelManager::processLevel}
     * @see {@code LevelManager::resetLevel}
     */
    public void startLevel(Level level) throws Exception {
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

    @Override
    public void startCurrentLevel() throws Exception {
        startLevel(getCurrentLevel());
    }

    @Override
    public long getPlayerScore() {
        return playerScore;
    }

    @Override
    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    @Override
    public void addToScore(long plus) {
        this.playerScore += plus;
    }

    /**
     * Initializing the basic structure of all levels.
     *
     * @throws Exception if something goes wrong during the process of building
     * or reading from a database
     */
    @Override
    public void initLevels() throws Exception {
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
