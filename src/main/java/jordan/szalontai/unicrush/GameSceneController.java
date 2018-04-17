package jordan.szalontai.unicrush;

import java.io.IOException;
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
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that controls the main JavaFx {@code Scene} on which we play the
 * current level of a {@code Game}.
 *
 * @author Szalontai Jordán
 */
public class GameSceneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameSceneController.class);

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
    private String hightlightedCoors;

    private Thread popTaskThread;
    private Thread helpTaskThread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            game = new CandyCrushGame(0);
            game.initLevels();

            setMainGridDimensions(game.getCurrentLevel());

            game.startCurrentLevel();

            levelSteps.setText(Integer.toString(game.getCurrentLevel().getAvailableSteps()));

            hightlightedCoors = "";
            firstRender(game.getCurrentLevel().getBoardState());

            startHelpTask();
            enableOnClicks();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public synchronized void showHelpMarkers() {
        LOGGER.debug("Showing help markers");
        hightlightedCoors = SimpleLevelManager.getInstance()
                .areThereAvailableMoves(game.getCurrentLevel());

        Integer[][] coordinates = LevelManager.processCoordinateString(hightlightedCoors);

        for (Integer[] coor : coordinates) {
            getFromGrid(coor[0], coor[1]).getStyleClass().add("highlighted");
        }
    }

    public synchronized void hideHelpMarkers() {
        LOGGER.debug("Hiding help markers");
        if (hightlightedCoors.equals("")) {
            return;
        }

        Integer[][] coordinates = LevelManager.processCoordinateString(hightlightedCoors);

        for (Integer[] coor : coordinates) {
            getFromGrid(coor[0], coor[1]).getStyleClass().removeAll("highlighted");
        }

        hightlightedCoors = "";
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

    /**
     * Creates a number of n*n new columns and rows for the main grid of the
     * {@code Scene}, where n is the size of the given {@code Level}'s board.
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

        LOGGER.trace("Selected candies: {}", Arrays.toString(selectedCandies));

        swapSelectedCandies(isSwapReady());
    }

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

    private void swapSelectedCandies(boolean ready) {
        if (ready) {
            clearMessage();
            disableOnClicks();

            Integer[][] coors = LevelManager.processCoordinateString(selectedCandies[0] + ";" + selectedCandies[1]);
            game.getCurrentLevel().swap(coors);
            renderCurrentLevel(game.getCurrentLevel().getBoardState());

            List<String> boardStates = SimpleLevelManager.getInstance().processWithState(game.getCurrentLevel());

            long[] result = Arrays.stream(boardStates.get(0)
                    .split(","))
                    .mapToLong(Long::parseLong).toArray();
            boardStates.remove(0);

            processChanges(coors, result, boardStates);
        }
    }

    private void processChanges(Integer[][] coors, long[] result, List<String> boardStates) {
        LOGGER.info("Cancelling help task ...");
        helpTaskThread.interrupt();

        final long iterations = result[0];
        final long add = result[1];

        if (iterations == CandyCrushGame.MAX_ITERATION) {
            LOGGER.info("Maximum iterations, reseting level ...");
            LevelManager.reset(game.getCurrentLevel());
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        if (iterations == 0) {
            boardStates.add(game.getCurrentLevel()
                    .getBoardState());
            game.getCurrentLevel().swap(coors);
            boardStates.add(game.getCurrentLevel()
                    .getBoardState());
        }

        game.addToScore(add);

        startPopTask(boardStates, add);

        eraseSelectedCandies(coors);
        enableOnClicks();
    }

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
                        LOGGER.error(ex.getMessage());
                    }
                }
                return stateIndex;
            }
        };
        popTask.setOnSucceeded(e -> {
            if (add >= 500) {
                setMessage(Level.getMessage());
            }

            setScore(game.getPlayerScore());

            if (add != 0) {
                decreaseAvailableSteps();
            }

            if (Integer.parseInt(scoreLabel.getText()) >= game.getCurrentLevel().getScoreToComplete()) {
                endLevel(true);
            }

            hideHelpMarkers();

            LOGGER.info("Restarting Help Task");
            startHelpTask();

            if (levelSteps.getText().equals("0")) {
                endLevel(false);
            }
        });

        popTaskThread = new Thread(popTask);
        popTaskThread.setName("Pop Task Thread");
        popTaskThread.setDaemon(true);

        popTaskThread.start();
    }

    private void startHelpTask() {
        Task<Integer> helpTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                LOGGER.trace("Help Task called");
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException ex) {
                    LOGGER.info("Interrupted sleep {}", this.getState().toString());
                    hideHelpMarkers();
                }
                return 0;
            }
        };
        helpTask.setOnSucceeded(e -> {
            LOGGER.debug("Highlighted coordinates: {}", hightlightedCoors);
            showHelpMarkers();
        });

        helpTaskThread = new Thread(helpTask);
        helpTaskThread.setName("Help Task Thread");
        helpTaskThread.setDaemon(true);

        helpTaskThread.start();
    }

    private boolean isSwapReady() {
        return selectedCandies[0] != null && selectedCandies[1] != null;
    }

    private Node getFromGrid(int i, int j) {
        return mainGrid.getChildren()
                .get(i * game.getCurrentLevel().getBoardSize() + j);
    }

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

    private void endLevel(boolean won) {
        Stage stage = (Stage) mainGrid.getScene().getWindow();
        try {
            if (won) {
                LOGGER.warn("GG");

                Main
                    .loadNewScene(stage, Main.SCENES[1], "Game Over")
                    .<EndGameController>getController()
                    .setGrat("Congratulations! Your score is " + game.getPlayerScore());

            } else {
                LOGGER.warn("STEPS 0");
                Main
                    .loadNewScene(stage, Main.SCENES[1], "Game Over")
                    .<EndGameController>getController()
                    .setGrat("There are no more steps! Your score was " + game.getPlayerScore());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
