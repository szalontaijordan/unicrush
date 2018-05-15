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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.CandyCrushGame;
import unicrush.model.Validator;
import unicrush.model.db.DAOFactory;
import unicrush.model.db.ScoreDAO;
import unicrush.model.db.ScoreEntity;
import unicrush.model.db.UserDAO;
import unicrush.model.db.UserEntity;

/**
 * FXML Controller class.
 *
 * @author szalontaijordan
 */
public class EndGameSceneController implements Initializable {

    //CHECKSTYLE:OFF
    private static final Logger LOGGER = LoggerFactory.getLogger(EndGameSceneController.class);

    @FXML
    private Label gratText;

    @FXML
    private Label currentScore;

    @FXML
    private Button newGame;

    @FXML
    private TableView<ScoreRow> scoreTable;

    private CandyCrushGame game;
    private Validator validator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.validator = Validator.getInstance();

        scoreTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        scoreTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("levelId"));
        scoreTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("score"));

        scoreTable.setEditable(false);
    }
    //CHECKSTYLE:ON

    /**
     * Initializes the labels of the scene, and sets the content of the high
     * score table.
     *
     * <p>
     * This method is usually called from another controller class.</p>
     *
     * @param message the message displayed on the top of the scene
     * @param game the game object, that contains information about the game
     *
     * @see GameSceneController#endGame()
     */
    public void init(String message, CandyCrushGame game) {
        this.game = game;
        LOGGER.info("Setting message to {}", message);
        gratText.setText(message);

        LOGGER.info("Creating DAO-s");
        ScoreDAO scoreDao = DAOFactory.getInstance().createScoreDAO();
        UserDAO userDao = DAOFactory.getInstance().createUserDAO();

        setLabelInfo(userDao, scoreDao);
    }

    //CHECKSTYLE:OFF
    private void setLabelInfo(UserDAO userDao, ScoreDAO scoreDao) {
        // this shold not be null, since we were playing the game before
        UserEntity user = userDao.findByName(game.getPlayerName()).get(0);

        LOGGER.info("User that was playing: {}", user.getUsername());
        int userId = user.getId();
        int levelId = this.game.getCurrentLevel().getID();
        int userScore = this.game.getPlayerScore();

        LOGGER.info("Setting current score and highscore");
        ScoreEntity found = scoreDao.find(userId, levelId);

        if (found == null) {
            scoreDao.create(userId, levelId, userScore);
            currentScore.setText("Your score is " + userScore + "");
        } else if (validator.isNewHighScore(found, userScore)) {
            currentScore.setText("New high score: " + userScore + "");
            scoreDao.update(userId, levelId, userScore);
        }

        setHighScoreTable(scoreDao.findAll());
    }
    //CHECKSTYLE:ON

    /**
     * Updates the {@code TableView} in the scene with the data fetched from the
     * database.
     *
     * @param scores the list containing entities from the database
     */
    public void setHighScoreTable(List<ScoreEntity> scores) {
        if (scores == null || scores.isEmpty()) {
            return;
        }

        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        List<ScoreRow> data = new ArrayList<>();
        scores.stream()
                .forEach(score -> {
                    final String userName = userDao.get(score.getUserId()).getUsername();
                    final Integer levelId = score.getLevelId();
                    final Integer newScore = score.getScore();
                    data.add(new ScoreRow(userName, levelId, newScore));
                });

        scoreTable.setItems(FXCollections.observableArrayList(data));
    }

    /**
     * When the user clicks the <em>new game</em> button, the game takes them
     * back to the log in screen.
     *
     * @param event the click event
     */
    @FXML
    public void onNewGame(ActionEvent event) {
        switchToLogInScene((Stage) newGame.getScene().getWindow());
    }

    //CHECKSTYLE:OFF
    private void switchToLogInScene(Stage stage) {
        try {
            Main.loadNewScene(stage, Main.SCENES[0], "Welcome")
                    .<LogInSceneController>getController()
                    .setGame(game);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
    //CHECKSTYLE:ON

    /**
     * Class for representing a table row on the end-game scene.
     */
    public static class ScoreRow {

        // CHECKSTYLE:OFF
        private final SimpleStringProperty userName;
        private final SimpleIntegerProperty levelId;
        private final SimpleIntegerProperty score;

        private ScoreRow(String userName, int levelId, int score) {
            this.userName = new SimpleStringProperty(userName);
            this.levelId = new SimpleIntegerProperty(levelId);
            this.score = new SimpleIntegerProperty(score);
        }

        public String getUserName() {
            return userName.get();
        }

        public Integer getLevelId() {
            return levelId.get();
        }

        public Integer getScore() {
            return score.get();
        }

        public void setUserName(String userName) {
            this.userName.set(userName);
        }

        public void setLevelId(Integer levelId) {
            this.levelId.set(levelId);
        }

        public void setScore(Integer score) {
            this.score.set(score);
        }
        //CHECKSTYLE:ON
    }
}
