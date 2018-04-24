package jordan.szalontai.unicrush;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
 * Class that controls the main JavaFx {@code Scene} on which we play a {@code Level} instance of a
 * {@code Game} object.
 *
 * @author Szalontai Jordán
 */
public class GameSceneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameSceneController.class);

    private static final int HELP_INTERVAL = 30000;
    private static final int POP_INTERVAL = 450;

    @FXML
    private GridPane mainGrid;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelMessage;
    @FXML
    private Label levelSteps;

    private Game game;
    private String[] selectedCandies;
    private String suggestedArea;

    private Thread popThread;
    private Timer suggestionTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        suggestedArea = "";
        selectedCandies = new String[2];

        suggestionTimer = new Timer("Highlight Thread", true);

        popThread = new Thread("Pop Task Thread");
        popThread.setDaemon(true);

        startGame();
    }

    private void startGame() {
        try {
            game = new CandyCrushGame();
            game.initLevels();
            game.startCurrentLevel();

            setMainGridDimensions(game.getCurrentLevel());
            firstRender(game.getCurrentLevel().getBoardState());

            levelSteps.setText(game.getCurrentLevel().getAvailableSteps() + "");
            startSuggestionTask();
            enableButtonClicks();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
    
    public void endLevel() {
        try {
            LOGGER.warn("Ending level...");
            popThread.interrupt();
            suggestionTimer.cancel();

            Stage stage = (Stage) mainGrid.getScene().getWindow();
            String message = "Congratulations!";

            if (game.getPlayerScore() < game.getCurrentLevel().getScoreToComplete()) {
                message = "There are no more steps!";
            }

            Main.loadNewScene(stage, Main.SCENES[1], "Game Over").<EndGameController>getController()
                    .setGrat(message + " Your score is " + game.getPlayerScore());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Creates a number of n*n new columns and rows for the main grid of the {@code Scene}, where n
     * is the size of the given {@code Level}'s board.
     *
     * @param level the {@code Level} instance from which we get the board size.
     */
    public void setMainGridDimensions(Level level) {
        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                mainGrid.getColumnConstraints().add(new ColumnConstraints());
            }
            mainGrid.getRowConstraints().add(new RowConstraints());
        }
    }
    
    private Node getNode(int i, int j) {
        return mainGrid.getChildren().get(i * game.getCurrentLevel().getBoardSize() + j);
    }

    private void enableButtonClicks() {
        mainGrid.getChildren().stream().forEach(b -> b.setOnMouseClicked(e -> onCandySelect(e)));
    }

    private void disableButtonClicks() {
        mainGrid.getChildren().stream().forEach(b -> b.setOnMouseClicked(null));
    }

    private void onCandySelect(MouseEvent e) {
        Button b = (Button) e.getSource();

        String newSelect = String.format("%s,%s",
                Integer.toString(GridPane.getRowIndex(b)),
                Integer.toString(GridPane.getColumnIndex(b))
        );

        for (int i = 0; i < 2; i++) {
            if (selectedCandies[i] == null) {
                selectedCandies[i] = newSelect;
                b.getStyleClass().add("selected");
                break;
            }
            if (selectedCandies[i].equals(newSelect)) {
                selectedCandies[i] = null;
                b.getStyleClass().removeAll("selected");
                break;
            }
        }

        LOGGER.trace("Selected candies: {}", Arrays.toString(selectedCandies));

        swapSelectedCandies(selectedCandies[0] != null && selectedCandies[1] != null);
    }

    private void eraseSelectedCandies(Integer[][] coors) {
        getNode(coors[0][0], coors[0][1]).getStyleClass().removeAll("selected");
        getNode(coors[1][0], coors[1][1]).getStyleClass().removeAll("selected");

        selectedCandies[0] = null;
        selectedCandies[1] = null;
    }

    private void swapSelectedCandies(boolean ready) {
        disableButtonClicks();
        if (ready) {
            levelMessage.setText("");
            
            String coordinate = selectedCandies[0] + ";" + selectedCandies[1];
            Integer[][] coors = LevelManager.createCoordinates(coordinate);
            
            game.getCurrentLevel().swap(coors);
            renderBoardState(game.getCurrentLevel().getBoardState());

            List<String> boardStates = SimpleLevelManager.getInstance()
                    .processWithState(game.getCurrentLevel());

            long[] result = Arrays.stream(boardStates.get(0).split(",")).mapToLong(Long::parseLong)
                    .toArray();

            boardStates.remove(0);
            processChanges(coors, result, boardStates);
        }
        enableButtonClicks();
    }

    private void processChanges(Integer[][] coors, long[] result, List<String> boardStates) {
        LOGGER.info("Cancelling help task ...");

        long iterations = result[0];
        long add = result[1];

        if (iterations == CandyCrushGame.MAX_ITERATION) {
            LOGGER.info("Maximum iterations, reseting level ...");
            LevelManager.reset(game.getCurrentLevel());
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        if (iterations == 0) {
            boardStates.add(game.getCurrentLevel().getBoardState());
            game.getCurrentLevel().swap(coors);
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        game.addToScore(add);

        startPopTask(boardStates, add);
        eraseSelectedCandies(coors);
    }

    private void startPopTask(List<String> boardStates, final long add) {
        Task<Integer> popTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return onPapTaskCalled(boardStates);
            }
        };
        popTask.setOnSucceeded(e -> onPopTaskSucceeded(add));

        popThread = new Thread(popTask);
        popThread.start();
    }

    private Integer onPapTaskCalled(List<String> boardStates) {
        int stateIndex = 0;

        while (stateIndex < boardStates.size()) {
            renderBoardState(boardStates.get(stateIndex++));
            try {
                Thread.sleep(POP_INTERVAL);
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return stateIndex;
    }
    
    private void onPopTaskSucceeded(final long add) {
        boolean isMaxScore = game.getPlayerScore() >= game.getCurrentLevel().getScoreToComplete();
        boolean isZeroSteps = game.getCurrentLevel().getAvailableSteps() == 0;

        scoreLabel.setText(game.getPlayerScore() + "");

        LOGGER.debug("Hiding help markers");
        hideSuggestionMarkers();

        if (add >= 500) {
            levelMessage.setText(Level.getMessage());
        }

        if (add != 0) {
            levelSteps.setText("" + (Integer.parseInt(levelSteps.getText()) - 1));
        }

        if (isMaxScore || isZeroSteps) {
            endLevel();
        }
    }

    private void startSuggestionTask() {
        suggestionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LOGGER.debug("Highlighted coordinates: {}", suggestedArea);
                showSuggestionMarkers();
            }
        }, HELP_INTERVAL, HELP_INTERVAL);
    }
    
    public void showSuggestionMarkers() {
        LOGGER.debug("Showing help markers");
        suggestedArea = SimpleLevelManager.getInstance().getAvailableMoves(game.getCurrentLevel());

        for (Integer[] coor : LevelManager.createCoordinates(suggestedArea)) {
            getNode(coor[0], coor[1]).getStyleClass().add("highlighted");
        }
    }

    public void hideSuggestionMarkers() {
        LOGGER.debug("Hiding help markers");

        for (Integer[] coor : LevelManager.createCoordinates(suggestedArea)) {
            getNode(coor[0], coor[1]).getStyleClass().removeAll("highlighted");
        }
    }

    private void firstRender(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                Button b = new Button();

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

    public void renderBoardState(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                if (!color.equals("x")) {
                    getNode(i, j).setStyle("-fx-background-image: " + Main.getCandyImageURL(color));
                }
            }
        }
    }
}
