package unicrush.controller;

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
import unicrush.model.LevelManager;
import unicrush.model.GridManager;
import java.io.IOException;
import java.net.URL;
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
    private GridManager gridManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            game = new CandyCrushGame();
            game.initLevels();
            game.startCurrentLevel();

            preprocessLevelWith(game.getCurrentLevel().getManager());

            gridManager = new GridManager(mainGrid, game);
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
        int iterations = game.getCurrentLevel().getManager().process();

        if (iterations == CandyCrushGame.MAX_ITERATION) {
            LOGGER.info("Maximum iteration, reseting level ...");
            game.getCurrentLevel().getManager().reset();
            game.getCurrentLevel().getManager().process();
        }
    }

    public void endLevel() {
        try {
            LOGGER.warn("Ending level...");
            gridManager.getPopThread().interrupt();
            gridManager.getSuggestionTimer().cancel();
            gridManager.getSuggestionTimer().purge();

            Stage stage = (Stage) mainGrid.getScene().getWindow();
            String message = "Congratulations!";

            if (game.getPlayerScore() < game.getCurrentLevel().getScoreToComplete()) {
                message = "There are no more steps!";
            }

            Main.loadNewScene(stage, Main.SCENES[1], "Game Over")
                    .<EndGameController>getController()
                    .setGrat(message + " Your score is " + game.getPlayerScore());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void onCandySelect(MouseEvent e) {
        Button b = (Button) e.getSource();
        gridManager.updateSelectedCandies(GridPane.getRowIndex(b), GridPane.getColumnIndex(b));

        LOGGER.trace("Selected candies: {}", gridManager.getSelectedCandies());

        gridManager.disableButtonClicks();
        if (gridManager.isSwapReady()) {
            levelMessage.setText("");

            String coordinate = gridManager.getSelectedCandies();
            Integer[][] coors = Level.createCoordinates(coordinate);

            game.getCurrentLevel().getManager().swap(coors);
            gridManager.renderBoardState(game.getCurrentLevel().getBoardState());

            List<String> boardStates = game.getCurrentLevel().getManager().processWithState();

            int[] result = {
                game.getCurrentLevel().getManager().getIterations(),
                game.getCurrentLevel().getManager().getSum()
            };

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
            game.getCurrentLevel().getManager().reset();
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        if (iterations == 0) {
            boardStates.add(game.getCurrentLevel().getBoardState());
            game.getCurrentLevel().getManager().swap(coors);
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        game.addToScore(add);

        gridManager.startPopTask(boardStates).setOnSucceeded(event -> onPopSuccess(add));
        gridManager.eraseSelectedCandies(coors);
    }

    private void onPopSuccess(final long add) {
        boolean isMaxScore = game.getPlayerScore() >= game.getCurrentLevel().getScoreToComplete();
        boolean isZeroSteps = levelSteps.getText().equals("0");

        if (add != 0) {
            levelSteps.setText("" + (Integer.parseInt(levelSteps.getText()) - 1));
        }
        if (add >= 500) {
            levelMessage.setText(Main.getMessage());
        }
        
        gridManager.hideSuggestionMarkers();
        scoreLabel.setText(game.getPlayerScore() + "");
        
        if (isMaxScore || isZeroSteps) {
            endLevel();
        }
    }
}
