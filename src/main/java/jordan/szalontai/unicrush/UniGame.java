package jordan.szalontai.unicrush;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;

public class UniGame {

    public static final int MAX_ITERATION = 50;

    private long playerScore;
    private int currentLevel;
    private List<Level> levels;

    public UniGame(int currentLevel) {
        this.playerScore = 0;
        this.levels = new ArrayList<>();
        this.currentLevel = currentLevel;
    }

    public void startCurrentLevel(GridPane mainGrid) throws Exception {
        startLevel(this.currentLevel, mainGrid);
    }

    public void startLevel(int currentLevel, GridPane mainGrid) throws Exception {
        Level current = getLevel(currentLevel);

        System.out.println("Preprocessing");
        
        int iteration = LevelManager.processLevel(current);

        if (iteration == MAX_ITERATION) {
            System.out.println("Maxit, preprocessing again");
            levels.set(currentLevel, new LevelBuilder(current)
                    .fillBoardRandom()
                    .create());

            LevelManager.processLevel(current);
        }

        System.out.println("Start:");
        System.out.println(current);

        this.playerScore = 0;
    }

    public Level getLevel(int index) {
        return levels.get(index);
    }
    
    public List<Level> getLevels() {
        return levels;
    }

    public long getPlayerScore() {
        return playerScore;
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public void addToScore(long plus) {
        this.playerScore += plus;
    }

    @Override
    public String toString() {
        return "gg";
    }

    public void initLevels() throws Exception {
        Level l = new LevelBuilder()
                .setCompliteScore(500)
                .setAvailableSteps(20)
                .setBoardSize(8)
                .putWallsFromString("0,0;1,0;6,0;7,0;0,7;1,7;6,7;7,7")
                .fillBoardRandom()
                .create();

        levels.add(l);
    }

}
