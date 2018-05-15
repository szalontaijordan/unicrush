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
import unicrush.model.Level;
import unicrush.model.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that controls the main JavaFx {@code Scene} on which we play a {@code Level} instance of a
 * {@code Game} object.
 *
 * @author Szalontai Jordán
 */
public class GameSceneController implements Initializable {

    //CHECKSTYLE:OFF
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSceneController.class);

    @FXML
    private GridPane mainGrid;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelMessage;
    @FXML
    private Label levelSteps;

    private CandyCrushGame game;
    private Validator validator;
    private GridManager gridManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    //CHECKSTYLE:ON

    /**
     * Initializes the game scene.
     *
     * <p>
     * This method starts the level selected by the user before, and initializes the grid manager
     * that controls the main grid of the game.</p>
     *
     * @param game the game object containing information about the player and the levels
     */
    public void init(CandyCrushGame game) {
        try {
            // we init the game in the other scene
            this.game = game;
            this.game.startCurrentLevel();
            validator = Validator.getInstance();

            preprocessLevelWith(game.getManager());

            gridManager = new GridManager(mainGrid, game);
            gridManager.firstRender();
            gridManager.enableButtonClicks(event -> onCandySelect(event));
            gridManager.startSuggestionTask();

            levelSteps.setText(game.getCurrentLevel().getAvailableSteps() + "");
            scoreLabel.setText(String.format("%5d / %5d",
                this.game.getPlayerScore(),
                this.game.getCurrentLevel().getScoreToComplete()));
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * Preprocesses the current level of the game with the given {@code LevelManager}.
     *
     * <p>
     * This is necessary, because in the generation of the board is random, so there can be a board
     * state where three or more candies match in a row or column.</p>
     *
     * @param levelManager the manager that processes the level.
     * @return {@code true} if no problems happened during the processing, {@code false} if
     * resetting the level was required
     */
    public boolean preprocessLevelWith(LevelManager levelManager) {
        LOGGER.info("Preprocessing current level ...");
        int iterations = game.getManager().process();

        if (validator.isMaxIterations(iterations)) {
            LOGGER.info("Maximum iteration, reseting level ...");
            game.getManager().reset();
            game.getManager().process();

            return false;
        }
        return true;
    }

    /**
     * Ends the game on the current level and switches to the end-game scene.
     */
    public void endGame() {
        LOGGER.warn("Ending game...");
        gridManager.getPopThread().interrupt();
        gridManager.getSuggestionTimer().cancel();
        gridManager.getSuggestionTimer().purge();

        switchToEndGameScene();
    }

    //CHECKSTYLE:OFF
    private void switchToEndGameScene() {
        try {
            Stage stage = (Stage) mainGrid.getScene().getWindow();
            String message = "Congratulations, " + game.getPlayerName() + "!";

            if (validator.isNoMoreSteps(Integer.parseInt(levelSteps.getText()))) {
                message = "There are no more steps, " + game.getPlayerName() + "!";
            }

            Main.loadNewScene(stage, Main.SCENES[2], "Game Over")
                    .<EndGameSceneController>getController()
                    .init(message, game);
        } catch (NumberFormatException | IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    //CHECKSTYLE:ON

    /**
     * Selects a candy on the displayed board, then tries to swap it with the other selected one.
     *
     * @param event the mouse event object
     */
    public void onCandySelect(MouseEvent event) {
        Button b = (Button) event.getSource();
        gridManager.updateSelectedCandies(GridPane.getRowIndex(b), GridPane.getColumnIndex(b));

        LOGGER.trace("Selected candies: {}", gridManager.getSelectedCandies());

        if (validator.isTwoSelected(gridManager.getSelectedCandies())) {
            levelMessage.setText("");
            swapSelectedCandies(gridManager.getSelectedCandies());
        }
    }

    //CHECKSTYLE:OFF
    private void swapSelectedCandies(String template) {
        Integer[][] coors = Level.createCoordinates(template);
        LOGGER.debug("CREATED COORS: {}", Arrays.toString(coors));
        game.getManager().swap(coors);
        gridManager.renderBoardState(game.getCurrentLevel().getBoardState());

        List<String> boardStates = game.getManager().processWithState();

        int boardIterations = game.getManager().getIterations();
        int boardSum = game.getManager().getSum();

        showCanges(coors, boardIterations, boardSum, boardStates);
    }

    private void showCanges(Integer[][] coors, int iterations, int sum, List<String> boardStates) {
        LOGGER.info("Showing changes ...");

        if (validator.isNoIterations(iterations)) {
            boardStates.add(game.getCurrentLevel().getBoardState());
            game.getManager().swap(coors);
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        if (validator.isMaxIterations(iterations)) {
            LOGGER.info("Maximum iterations, reseting level ...");
            game.getManager().reset();
            boardStates.add(game.getCurrentLevel().getBoardState());
        }

        game.addToScore(sum);

        gridManager.startPopTask(boardStates).setOnSucceeded(event -> onPopSuccess(sum));
        gridManager.eraseSelectedCandies();
    }

    private synchronized void onPopSuccess(final long add) {
        if (add != 0) {
            levelSteps.setText("" + (Integer.parseInt(levelSteps.getText()) - 1));
        }
        if (add >= 500) {
            levelMessage.setText(Main.getMessage());
        }

        gridManager.hideSuggestionMarkers();
        scoreLabel.setText(String.format("%5d / %5d",
                game.getPlayerScore(),
                game.getCurrentLevel().getScoreToComplete()));

        if (validator.isEndGameSituation(game, Integer.parseInt(levelSteps.getText()))) {
            endGame();
        }
    }
    //CHECKSTYLE:ON
}
