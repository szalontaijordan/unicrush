package jordan.szalontai.unicrush;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * A class that controls the main JavaFx {@code Scene} on which we play the
 * current level of a {@code Game}.
 *
 * @author Szalontai Jordán
 */
public class UniGameController implements Initializable {

    /**
     * The possible game states, a game can be won or lost.
     */
    private static enum GameState {
        WON,
        LOST
    };

    @FXML
    private GridPane mainGrid;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelMessage;
    @FXML
    private Label levelSteps;

    private Game game;
    private String[] selectedCandies = new String[2];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            game = new UniGame(0);
            game.initLevels();

            setMainGridDimensions(game.getLevel(game.getCurrentLevel()));

            game.startCurrentLevel();
            levelSteps.setText(Integer.toString(game.getLevel(game.getCurrentLevel()).getAvailableSteps()));

            firstRender(game.getLevel(game.getCurrentLevel()).getBoardState());

            enableOnClicks();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void setScore(long score) {
        scoreLabel.setText(Long.toString(score));
    }

    public synchronized void setMessage(String message) {
        levelMessage.setText(message);
    }

    public synchronized void decreaseAvailableSteps() {
        levelSteps.setText(Integer.toString(Integer.parseInt(levelSteps.getText()) - 1));
    }

    public synchronized void clearMessage() {
        levelMessage.setText("");
    }

    private Node getFromGrid(int i, int j) {
        return mainGrid.getChildren()
                .get(i * game.getLevel(game.getCurrentLevel()).getBoardSize() + j);
    }

    /**
     * A method creating a number of n*n new columns and rows for the main grid
     * of the {@code Scene}, where n is the size of the given {@code Level}'s
     * board.
     *
     * @param l the {@code Level} instance from which we get the board size.
     */
    public void setMainGridDimensions(Level l) {
        List<ColumnConstraints> cols = new ArrayList<>();
        List<RowConstraints> rows = new ArrayList<>();

        for (int i = 0; i < l.getBoardSize(); i++) {
            for (int j = 0; j < l.getBoardSize(); j++) {
                cols.add(new ColumnConstraints());
            }
            rows.add(new RowConstraints());
        }

        mainGrid.getColumnConstraints().addAll(cols);
        mainGrid.getRowConstraints().addAll(rows);
    }

    private void enableOnClicks() {
        mainGrid.getChildren().stream()
                .forEach(b -> b.setOnMouseClicked(e -> updateSelectedCandies(e)));
    }

    private void disableOnClicks() {
        mainGrid.getChildren().stream()
                .forEach(b -> b.setOnMouseClicked(null));
    }

    /**
     * An event handler for tracking the candies selected on the grid.
     *
     * @param e the {@code MouseEvent} that occurred
     *
     * For further information see {@code swapSelectedCandies}
     */
    private void updateSelectedCandies(MouseEvent e) {
        Button b = (Button) e.getSource();

        String newSelect = String.format("%s,%s",
                Integer.toString(GridPane.getRowIndex(b)),
                Integer.toString(GridPane.getColumnIndex(b))
        );

        for (int c = 0; c < 2; c++) {
            if (selectedCandies[c] == null) {
                selectedCandies[c] = newSelect;
                b.getStyleClass().add("selected");
                break;
            }
            if (selectedCandies[c].equals(newSelect)) {
                selectedCandies[c] = null;
                b.getStyleClass().removeAll("selected");
                break;
            }
        }

        System.out.println(Arrays.toString(selectedCandies));

        swapSelectedCandies(isSwapReady());
    }

    /**
     * A helper method for reseting the array that is keeping track of the
     * selected candies on a grid.
     *
     * @param coors an array representing the coordinates of the candies on the
     * grid, so we can manage their CSS
     */
    private void eraseSelectedCandies(Integer[][] coors) {
        getFromGrid(coors[0][0], coors[0][1])
                .getStyleClass()
                .removeAll("selected");
        getFromGrid(coors[1][0], coors[1][1])
                .getStyleClass()
                .removeAll("selected");

        selectedCandies[0] = null;
        selectedCandies[1] = null;
    }

    /**
     * Swapping the two successfully selected candies of the grid.
     *
     * After the swap happened, the processing of the {@code Level} must be done
     * due to the logic of the game. While this processing is taking place,
     * onClick handles are disabled (set to {@code nul})
     *
     * @param ready the flag that indicates if the potential two candies are
     * ready for swap
     *
     * For further information see {@code startPopTask}
     * and {@code LevelManager.processLevelWithState}
     */
    private void swapSelectedCandies(boolean ready) {
        if (ready) {
            clearMessage();
            disableOnClicks();

            Integer[][] coors = LevelBuilder.processCoordinateString(selectedCandies[0] + ";" + selectedCandies[1]);
            game.getLevel(game.getCurrentLevel()).swap(coors);
            renderCurrentLevel(game.getLevel(game.getCurrentLevel()).getBoardState());

            List<String> boardStates = new ArrayList<>();

            long[] result = LevelManager.processLevelWithState(game.getLevel(game.getCurrentLevel()), boardStates);

            final long iterations = result[0];
            final long add = result[1];

            if (iterations == UniGame.MAX_ITERATION) {
                System.out.println("MAXIT");
                LevelManager.resetLevel(game.getLevel(game.getCurrentLevel()));
                boardStates.add(game.getLevel(game.getCurrentLevel()).getBoardState());
            }

            if (iterations == 0) {
                boardStates.add(game.getLevel(game.getCurrentLevel())
                        .getBoardState());
                game.getLevel(game.getCurrentLevel()).swap(coors);
                boardStates.add(game.getLevel(game.getCurrentLevel())
                        .getBoardState());
            }

            game.addToScore(add);

            startPopTask(boardStates, add);

            eraseSelectedCandies(coors);
            enableOnClicks();
        }
    }

    /**
     * Starting a new {@code Thread} with a JavaFx {@code Task}, that modifies
     * the displayed appearance of the candies.
     *
     * @param boardStates the {@code List} containing the states of the current
     * level that it was in during the processing
     * @param add the points the player will gain after the successful
     * processing of the current level
     */
    private void startPopTask(List<String> boardStates, final long add) {
        Task<Integer> popTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                int stateIndex = 0;

                while (stateIndex < boardStates.size()) {
                    renderCurrentLevel(boardStates.get(stateIndex++));
                    try {
                        Thread.sleep(450);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                return stateIndex;
            }
        };
        popTask.setOnSucceeded(e -> {
            if (add >= 500) {
                setMessage(Level.getMessage());
            }

            if (Integer.parseInt(scoreLabel.getText()) == game.getLevel(game.getCurrentLevel()).getScoreToComplete()) {
                endLevel(GameState.WON);
            }

            if (levelSteps.getText().equals("0")) {
                endLevel(GameState.LOST);
            }

            if (add >= 0) {
                setScore(game.getPlayerScore());
                decreaseAvailableSteps();
            }
        });
        new Thread(popTask).start();
    }

    private boolean isSwapReady() {
        return selectedCandies[0] != null && selectedCandies[1] != null;
    }

    /**
     * Setting the constraints of the main grid with new buttons based on the
     * current level's board state.
     *
     * @param boardState the {@code String} representing a {@code Level}'s
     * state.
     */
    private void firstRender(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                Button b = new Button();

                b.setPrefWidth(30);
                b.setPrefHeight(30);

                if (!color.equals("x")) {
                    b.setStyle("-fx-background-image:" + Main.getCandyImageURL(color));
                } else {
                    b.setStyle("-fx-background-color: transparent");
                }

                GridPane.setConstraints(b, j, i);
                mainGrid.getChildren().add(b);
            }
        }
    }

    /**
     * Setting the CSS attributes of each {@code Node} on the main grid based on
     * the given board state.
     *
     * @param boardState the {@code String} representing a {@code Level}'s
     * state.
     */
    private void renderCurrentLevel(String boardState) {
        String[] state = boardState.split(";");
        int boardSize = state.length;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                if (!color.equals("x")) {
                    mainGrid.getChildren()
                            .get(i * boardSize + j)
                            .setStyle("-fx-background-image: " + Main.getCandyImageURL(color));
                }
            }
        }
    }

    /**
     * Endgame logic based on the {@code GameState} we called it with.
     * 
     * We can either lose a game or win.
     * 
     * @param state representing the state in which the game has ended
     */
    private void endLevel(GameState state) {
        switch (state) {
            case WON:
                System.out.println("GG");
                break;
            case LOST:
                System.out.println("STEPS 0");
                break;
            default:
                break;
        }
    }
}
