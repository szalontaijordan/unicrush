package jordan.szalontai.unicrush;

import java.util.Arrays;

public class StandardLevel extends Level {

    public StandardLevel(int scoreToComplete, int steps, int boardSize, String walls) {
        super(scoreToComplete, steps, boardSize, walls);
        this.walls = LevelBuilder.processCoordinateString(walls);
        
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Candy(Candy.getRandomColorState());
            }
        }

        Arrays.stream(this.walls)
                .forEach(wall -> board[wall[0]][wall[1]] = null);
    }

    public StandardLevel() {
    }
       
}
