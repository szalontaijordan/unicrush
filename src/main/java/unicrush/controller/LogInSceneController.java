package unicrush.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.CandyCrushGame;
import unicrush.model.Level;
import unicrush.model.Validator;
import unicrush.model.db.DAOFactory;
import unicrush.model.db.UserDAO;
import unicrush.model.db.UserEntity;

/**
 * FXML Controller class.
 *
 * @author Szalontai Jordán
 */
public class LogInSceneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInSceneController.class);

    @FXML
    Label errorLabel;

    @FXML
    TextField login;

    @FXML
    MenuButton levelSelect;

    @FXML
    Button start;

    CandyCrushGame game;
    Validator validator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validator = Validator.getInstance();

        try {
            game = new CandyCrushGame();
            game.initLevels();

            game.getLevels().forEach(level -> {
                MenuItem item = new MenuItem(""+level.getID());
                item.setOnAction(e -> levelSelect.setText(item.getText()));
                levelSelect.getItems().add(item);
            });

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    @FXML
    public void startGame(ActionEvent event) {
        String name = login.getText();
        if (validator.isEmptyString(name)) {
            errorLabel.setText("Invalid username!");
            return;
        }

        String selectedLevel = levelSelect.getText();
        if (validator.isEmptyString(selectedLevel)) {
            errorLabel.setText("Please select a level!");
            return;
        }
        
        int id = Integer.parseInt(selectedLevel);
        Level levelToStart = game.getLevels().stream()
                .filter(level -> level.getID() == id)
                .findFirst()
                .get();
        
        registerUser(name);
        switchToGameScene(levelToStart);
    }

    private void registerUser(String name) {
        UserDAO userDao = DAOFactory.getInstance().createUserDAO();
        List<UserEntity> users = userDao.findByName(name);
        if (users.isEmpty()) {
            userDao.create(name);
            LOGGER.info("Username created with name: {}", name);
        }
        
        game.setPlayerName(name);
    }

    private void switchToGameScene(Level levelToStart) {
        try {
            game.setCurrentLevelIndex(game.getLevels().indexOf(levelToStart));
            Stage stage = (Stage) start.getScene().getWindow();

            Main.loadNewScene(stage, Main.SCENES[1], "UniCrush")
                    .<GameSceneController>getController()
                    .init(game);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
