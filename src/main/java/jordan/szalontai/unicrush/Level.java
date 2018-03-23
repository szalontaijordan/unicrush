package jordan.szalontai.unicrush;

import java.util.Arrays;

public class Level {

    public static final String[] MESSAGES = {"SWEET", "DELICIOUS", "DIVINE", "TASTY"};
    
    private int boardSize;
    private int availableSteps;
    private int scoreToComplete;
    private boolean transposed;
    private Integer[][] walls;
    private Candy[][] board;

    public Level(int scoreToComplete, int steps, int boardSize, String walls) {
        this.scoreToComplete = scoreToComplete;
        this.availableSteps = steps;
        this.boardSize = boardSize;
        this.board = new Candy[boardSize][boardSize];
        this.transposed = false;
        this.walls = LevelBuilder.processCoordinateString(walls);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Candy(Candy.getRandomColor());
            }
        }

        Arrays.stream(this.walls)
                .forEach(wall -> board[wall[0]][wall[1]] = null);
    }

    public Level() {
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getAvailableSteps() {
        return availableSteps;
    }

    public int getScoreToComplete() {
        return scoreToComplete;
    }

    public Integer[][] getWalls() {
        return walls;
    }

    public Candy[][] getBoard() {
        return board;
    }
    
    public boolean isTransposed() {
        return transposed;
    }
    
    public Candy get(int i, int j) {
        try {
            if (transposed) {
                return board[j][i];
            } else {
                return board[i][j];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    public void transpose() {
        this.transposed = !this.transposed;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setAvailableSteps(int availableSteps) {
        this.availableSteps = availableSteps;
    }

    public void setScoreToComplete(int scoreToComplete) {
        this.scoreToComplete = scoreToComplete;
    }

    public void setWalls(Integer[][] walls) {
        this.walls = walls;
    }

    public void setBoard(Candy[][] board) {
        this.board = board;
    }

    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException {
        if (transposed) {
            board[j][i] = c;
        } else {
            board[i][j] = c;
        }
    }

    public boolean swap(Integer[][] coors) {
        boolean success = testSwap(coors);

        if (!success) {
            return false;
        }

        Candy tmp = board[coors[0][0]][coors[0][1]];
        board[coors[0][0]][coors[0][1]] = board[coors[1][0]][coors[1][1]];
        board[coors[1][0]][coors[1][1]] = tmp;

        return true;
    }

    private boolean testSwap(Integer[][] coors) {
        return Math.abs(coors[0][0] - coors[1][0]) + Math.abs(coors[0][1] - coors[1][1]) == 1;
    }

    public String getBoardState() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                        .replaceAll("\\W", "")
                        .replaceAll("null", "x"))
                .reduce("", (sum, current) -> sum.concat(current).concat(";"));
    }
    
    @Override
    public String toString() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("null", "x")
                .replaceAll("E", " "))
                .reduce("", (output, currentRow) -> output += currentRow + "\n");
    }
    
    public static String getMessage() {
        return MESSAGES[(int) (Math.random() * MESSAGES.length)];
    }
}
