package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 * Class representing a standard level with no extra logic but basic holes.
 *
 * A subclass of the abstract {@code Level}
 *
 * @author Szalontai Jordán
 */
public class StandardLevel extends Level {

    /**
     * Constructs a {@code StandardLevel} instance with the constructor of the
     * superclass {@code Level} and fills up the board with random {@code Candy}
     * instances and the given walls.
     *
     * @param scoreToComplete the score required to complete the level
     * @param steps the maximum steps that can be used on this level
     * @param boardSize the dimensions of the board, a {@code Level}'s board is
     * always quadratic
     * @param walls the template {@code String} representing the coordinates of
     * the walls
     *
     * @see LevelBuilder#processCoordinateString(java.lang.String)
     */
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

        this.initialState = Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("\\W", "")
                .replaceAll("null", "x"))
                .reduce("", (sum, current) -> sum.concat(current).concat(";"));
    }

    /**
     * Constructs an empty {@code StandardLevel} object with the empty
     * constructor of the superclass.
     */
    public StandardLevel() {
        super();
    }
}
