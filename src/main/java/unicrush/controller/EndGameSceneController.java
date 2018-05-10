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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.CandyCrushGame;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EndGameSceneController.class);

    @FXML
    private Label gratText;

    @FXML
    private Label highScores;

    @FXML
    private Label currentScore;

    @FXML
    private Button newGame;

    private CandyCrushGame game;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init(String message, CandyCrushGame game) {
        this.game = game;
        LOGGER.info("Setting message to {}", message);
        gratText.setText(message);

        LOGGER.info("Creating DAO-s");
        ScoreDAO scoreDao = DAOFactory.getInstance().createScoreDAO();
        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        // this shold not be null, since we were playing the game before
        UserEntity user = userDao.findByName(game.getPlayerName()).get(0);

        LOGGER.info("User that was playing: {}", user.getUsername());

        int userId = user.getId();
        int levelId = this.game.getCurrentLevel().getID();
        int userScore = this.game.getPlayerScore();

        if (scoreDao.find(userId, levelId) == null) {
            scoreDao.create(userId, levelId, userScore);
        } else {
            scoreDao.update(userId, levelId, userScore);
        }

        LOGGER.info("Setting current score and highscore");
        currentScore.setText("Your score: " + this.game.getPlayerScore() + "");
        highScores.setText("Highscores:\n" + highScoreTableFromList(scoreDao.findAll()));
    }

    private String highScoreTableFromList(List<ScoreEntity> scores) {
        if (scores == null) {
            return "There are no highscores yet.";
        }

        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        String table = "";
        for (int i = 0; i < scores.size() && i < 10; i++) {
            ScoreEntity score = scores.get(i);
            table += userDao.get(score.getUserId()).getUsername() + "\t" + score.getScore() + "\n";
        }
        return table;
    }

    @FXML
    public void onNewGame(ActionEvent event) {
        Stage stage = (Stage) highScores.getScene().getWindow();

        switchToLogInScene(stage);
    }

    private void switchToLogInScene(Stage stage) {
        try {
            Main.loadNewScene(stage, Main.SCENES[0], "Welcome")
                    .<LogInSceneController>getController()
                    .setGame(game);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
