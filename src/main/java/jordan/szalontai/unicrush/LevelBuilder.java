package jordan.szalontai.unicrush;

import java.util.Arrays;

public abstract class LevelBuilder {

    protected Level l;
    
    public LevelBuilder() {}
    
    public LevelBuilder(Level l) {
        this.l = l;
    }

    public LevelBuilder setBoardSize(int size) throws Exception {
        if (size < 2) {
            throw new Exception("Too small board!");
        }
        l.setBoardSize(size);
        return this;
    }

    public LevelBuilder setAvailableSteps(int steps) throws Exception {
        if (steps < 1) {
            throw new Exception("Steps must be positive");
        }
        l.setAvailableSteps(steps);
        return this;
    }

    public LevelBuilder setCompliteScore(int score) throws Exception {
        if (score < 1) {
            throw new Exception("Steps must be positive");
        }
        l.setScoreToComplete(score);
        return this;
    }

    public LevelBuilder putWallsFromString(String template) {
        l.setWalls(StandardLevelBuilder.processCoordinateString(template));
        return this;
    }
    
    public abstract LevelBuilder fillBoard() throws Exception;
    
    public Level create() {
        return l;
    }
    
    /**
     * Creating a 2D-array that has coordinates that indicate locations on a
     * board.
     *
     * @param walls A String with the pattern. An example String: "0,0;0,1;0,2".
     * @return the 2D-array containing the coordinates
     */
    public static Integer[][] processCoordinateString(String walls) {
        return Arrays.stream(walls.split(";"))
                .map(coor -> Arrays.stream(coor.split(","))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
