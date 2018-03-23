package jordan.szalontai.unicrush;

import java.util.Arrays;

public class LevelBuilder {

    private Level l;

    public LevelBuilder() {
        this.l = new Level();
    }
    
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
        l.setWalls(processCoordinateString(template));
        return this;
    }

    public LevelBuilder fillBoardRandom() throws Exception {
        if (l.getBoardSize() == 0) {
            throw new Exception("Board size must be set first!");
        }

        if (l.getWalls() == null) {
            throw new Exception("Walls must be set first!");
        }

        if (l.getBoard() == null) {
            l.setBoard(new Candy[l.getBoardSize()][l.getBoardSize()]);
        }

        for (int i = 0; i < l.getBoardSize(); i++) {
            for (int j = 0; j < l.getBoardSize(); j++) {
                l.set(i, j, new Candy(Candy.getRandomColor()));
            }
        }

        Arrays.stream(l.getWalls())
                .forEach(wall -> l.set(wall[0], wall[1], null));

        return this;
    }

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
        return Arrays.stream(walls.split(";")).map(coor -> Arrays.stream(coor.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new)).toArray(Integer[][]::new);
    }
}
