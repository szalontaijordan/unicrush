package unicrush.model;

/*-
 * #%L
 * unicrush
 * %%
 * Copyright (C) 2018 Faculty of Informatics
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing a level of a game.
 *
 * @author Szalontai Jordán
 */
public final class Level implements Transposable {

    /**
     * Enum for the level types.
     *
     * @author Szalontai Jordán
     */
    public enum Type {

        STANDARD, JELLY, INGREDIENT
    }

    private final int boardSize;
    private final int scoreToComplete;
    private final int ID;
    private final Type type;
    private final String initialState;
    private final Integer[][] walls;
    private final Candy[][] board;

    private int availableSteps;
    private boolean transposed;

    /**
     * Constructs a level with the basic fields initialized.
     *
     * @param scoreToComplete the score required to complete the level
     * @param steps the maximum steps that can be used on this level
     * @param boardSize the dimensions of the board, a {@code Level}'s board is always quadratic
     * @param walls the template {@code String} representing the coordinates of the walls
     */
    private Level(Builder builder) {
        this.ID = builder.ID;
        this.boardSize = builder.boardSize;
        this.scoreToComplete = builder.scoreToComplete;
        this.type = builder.type;
        this.initialState = builder.initialState;
        this.walls = builder.walls;
        this.board = builder.board;
        this.availableSteps = builder.availableSteps;
        this.transposed = builder.transposed;
    }

    /**
     * Represents the board with a {@code String} that can be processed easily.
     *
     * <p>
     * Example usage</p>
     * <pre>
     * We have a <i>Level</i>:
     *
     *     [x, R, B, x]
     *     [G, G, B, G]
     *     [B, R, Y, G]
     *     [x, B, B, x]
     *
     * The result of <i>getBoardState()</i> is the following if not transposed:
     *
     *     xRBx;GGBG;BRYG;xBBx
     *
     * Othervise:
     *
     *     xGBx;RGRB;BBYB;xGGx
     *
     * </pre>
     *
     * @return the board state {@code String} produced as mentioned above
     */
    public String getBoardState() {
        String state = "";

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                state += get(i, j) == null ? "x" : get(i, j).toString();
            }
            state += ";";
        }
        return state.substring(0, state.length() - 1);
    }

    @Override
    public void transpose() {
        this.transposed = !this.transposed;
    }

    @Override
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

    @Override
    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException {
        if (transposed) {
            board[j][i] = c;
        } else {
            board[i][j] = c;
        }
    }

    /**
     * Returns a {@code String} representation of {@code this} instance.
     *
     * <pre>
     * Example:
     *
     *     [x, R, B, x]
     *     [G, G, B, G]
     *     [B, R, Y, G]
     *     [x, B, B, x]
     * </pre>
     *
     * @return the {@code String} representation of {@code this} instance as the example shows above
     */
    @Override
    public String toString() {
        return Arrays.stream(board)
                .map(row -> Arrays.toString(row)
                .replaceAll("null", "x")
                .replaceAll("E", " ")
                )
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns a 2D-array that represents coordinates.
     *
     * <p>
     * The idea of this, is that in the template we list some coordinates separated, and we parse
     * this template string. This method ignores whitesapce between the coordinates, because it is
     * using a regular expression pattern.</p>
     * <p>
     * For example {@code Level.Builder.createCoordinates("1,2 ; 3,4 ; 5,6");} returns
     * </p>
     *
     * <pre>
     *     new Integer[][]{ { 1, 2 }, { 3, 4 }, { 5, 6 } }
     * </pre>
     *
     * @param template a string that represents coordinates like in the example above
     * @return a 2D-array that represents coordinates
     */
    public static Integer[][] createCoordinates(String template) {
        if (!Validator.getInstance().isTemplate(template)) {
            return null;
        }
        template = template.replaceAll("\\s*", "");
        
        return Arrays.stream(template.split(";"))
                .filter(coor -> coor.length() > 0)
                .map(coor -> Arrays.stream(coor.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
    
    public int getBoardSize() {
        return boardSize;
    }

    public int getScoreToComplete() {
        return scoreToComplete;
    }

    public Type getType() {
        return type;
    }

    public String getInitialState() {
        return initialState;
    }

    public Integer[][] getWalls() {
        return walls;
    }

    public Candy[][] getBoard() {
        return board;
    }

    public int getAvailableSteps() {
        return availableSteps;
    }

    public boolean isTransposed() {
        return transposed;
    }

    public int getID() {
        return ID;
    }    

    /**
     * Static class for the level that follows the builder pattern.
     */
    public static class Builder {

        private final Type type;
        private final int boardSize;
        private int ID;
        private int scoreToComplete;
        private String initialState;
        private Integer[][] walls;
        private Candy[][] board;

        private int availableSteps;
        private boolean transposed;

        /**
         * Constructs a builder for a level, that will represent some pieces of essential
         * information about the level.
         *
         * @param type the type of the level
         * @param boardSize the size of the level (all levels are quadratic)
         * @throws IllegalArgumentException if the {@code type} is {@code null} or the
         * {@code boardSize} is less than 2
         */
        public Builder(int ID, Type type, int boardSize) throws IllegalArgumentException {
            if (type == null || boardSize < 2) {
                throw new IllegalArgumentException("Invalid level type, or too small board!");
            }
            this.ID = ID;
            this.type = type;
            this.boardSize = boardSize;
            this.transposed = false;
        }

        /**
         * Sets the score that is required to complete this level.
         *
         * @param score the new score
         * @return {@code this} so we can chain builder methods
         */
        public Builder withCompleteScore(final int score) {
            this.scoreToComplete = score;
            return this;
        }

        /**
         * Sets the amount of steps that are allowed on this level.
         *
         * @param steps the steps that are available
         * @return {@code this} so we can chain builder methods
         */
        public Builder withAvailableSteps(final int steps) {
            this.availableSteps = steps;
            return this;
        }

        /**
         * Sets the walls array.
         *
         * @param walls the walls array
         * @return {@code this} so we can chain builder methods
         */
        public Builder putWalls(final Integer[][] walls) {
            this.walls = walls;
            return this;
        }

        /**
         * Sets the board 2D-array with random {@code Candy} instances.
         *
         * <p>
         * It is important that you must put walls first into a level, so only the real parts of the
         * board will be filled.</p>
         *
         * @return {@code this} so we can chain builder methods
         */
        public Builder fillBoard() throws IllegalArgumentException {
            this.board = fillUpRandom();
            return this;
        }

        /**
         * Sets the board 2D-array with {@code Candy} instances specified in a template string.
         *
         * <p>
         * It is important that you must put walls first into a level, and the template string must
         * be consistent with these walls</p>
         *
         * @param template the template string representing information about the new board
         * @return {@code this} so we can chain builder methods
         */
        public Builder fillBoard(String template) throws IllegalArgumentException {
            this.board = fillUpFromState(template);
            return this;
        }

        /**
         * Builds a new {@code Level} object.
         *
         * <p>
         * This method also sets the {@code initialState} for the level, so we can refer to it in
         * the future.</p>
         *
         * @return a {@code Level} object with the builded fields
         */
        public Level build() {
            if (this.board == null) {
                throw new IllegalArgumentException("Board is not set yet.");
            }
            if (this.initialState == null || this.initialState.equals("")) {
                this.initialState = setupInitialState();
            }

            return new Level(this);
        }

        private String setupInitialState() {
            return Arrays.stream(board)
                    .map(row -> Arrays.toString(row)
                    .replaceAll("\\W", "")
                    .replaceAll("null", "x")
                    )
                    .collect(Collectors.joining(";"));
        }

        private Candy[][] fillUpRandom() {
            Candy[][] newBoard = new Candy[boardSize][boardSize];

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    newBoard[i][j] = new Candy(Candy.getRandomColorState());
                }
            }

            if (walls != null) {
                Arrays.stream(walls).forEach(wall -> newBoard[wall[0]][wall[1]] = null);
            }

            return newBoard;
        }

        private Candy[][] fillUpFromState(String template) throws IllegalArgumentException {
            Candy[][] newBoard = new Candy[boardSize][boardSize];
            String[] state = template.split(";");

            if (state.length != state[0].length()) {
                throw new IllegalArgumentException("Malformed board state string");
            }

            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[i].length(); j++) {
                    if (state[i].charAt(j) == 'x') {
                        newBoard[i][j] = null;
                    } else {
                        newBoard[i][j] = new Candy(Candy.getStateFromChar(state[i].charAt(j)));
                    }
                }
            }
            return newBoard;
        }
    }
}
