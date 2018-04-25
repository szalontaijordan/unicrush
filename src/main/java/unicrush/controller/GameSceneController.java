package unicrush.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import unicrush.model.CandyCrushGame;
import unicrush.model.Game;
import unicrush.model.Level;

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

    @FXML
    private GridPane mainGrid;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelMessage;
    @FXML
    private Label levelSteps;

    private Game game;
    private LevelManager levelManager;
    private GameGridManager gridManager;

    // TODO put this in the GameGridManager
    private String[] selectedCandies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            selectedCandies = new String[2];

            game = new CandyCrushGame();
            game.initLevels();
            game.startCurrentLevel();
            levelManager = SimpleLevelManager.getInstance();
            
            preprocessLevelWith(levelManager);

            gridManager = new GameGridManager(mainGrid, game);
            gridManager.setMainGridDimensions();
            gridManager.firstRender();
            gridManager.enableButtonClicks(event -> onCandySelect(event));
            gridManager.startSuggestionTask();

            levelSteps.setText(game.getCurrentLevel().getAvailableSteps() + "");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private void preprocessLevelWith(LevelManager levelManager) {
        LOGGER.info("Preprocessing current level ...");
        int iterations = levelManager.process(game.getCurrentLevel());
        
        if (iterations == CandyCrushGame.MAX_ITERATION) {
            LOGGER.info("Maximum iteration, reseting level ...");
            LevelManager.reset(game.getCurrentLevel());
            levelManager.process(game.getCurrentLevel());
        }
    }

    public void endLevel() {
        try {
            LOGGER.warn("Ending level...");
            gridManager.getPopThread().interrupt();
            gridManager.getSuggestionTimer().cancel();

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

    private void swapSelectedCandies(boolean ready) {
        gridManager.disableButtonClicks();
        if (ready) {
            levelMessage.setText("");

            String coordinate = selectedCandies[0] + ";" + selectedCandies[1];
            Integer[][] coors = Level.createCoordinates(coordinate);

            game.getCurrentLevel().swap(coors);
            gridManager.renderBoardState(game.getCurrentLevel().getBoardState());

            List<String> boardStates = levelManager.processWithState(game.getCurrentLevel());

            int[] result = Arrays.stream(boardStates.get(0).split(",")).mapToInt(Integer::parseInt)
                    .toArray();

            boardStates.remove(0);
            processChanges(coors, result, boardStates);
        }
        gridManager.enableButtonClicks(event -> onCandySelect(event));
    }

    private void processChanges(Integer[][] coors, int[] result, List<String> boardStates) {
        LOGGER.info("Cancelling help task ...");

        int iterations = result[0];
        int add = result[1];

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

        gridManager.startPopTask(boardStates, add);
        // TODO implement label updates
        eraseSelectedCandies(coors);
    }

    public void onPopSuccess(final long add) {
        boolean isMaxScore = game.getPlayerScore() >= game.getCurrentLevel().getScoreToComplete();
        boolean isZeroSteps = game.getCurrentLevel().getAvailableSteps() == 0;

        scoreLabel.setText(game.getPlayerScore() + "");

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

    private void eraseSelectedCandies(Integer[][] coors) {
        gridManager.getNode(coors[0][0], coors[0][1]).getStyleClass().removeAll("selected");
        gridManager.getNode(coors[1][0], coors[1][1]).getStyleClass().removeAll("selected");

        selectedCandies[0] = null;
        selectedCandies[1] = null;
    }
}
