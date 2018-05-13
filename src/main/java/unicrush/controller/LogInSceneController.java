package unicrush.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
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
 * FXML Controller class for the log in scene.
 *
 * @author Szalontai Jordán
 */
public class LogInSceneController implements Initializable {

    //CHECKSTYLE:OFF
    private static final Logger LOGGER = LoggerFactory.getLogger(LogInSceneController.class);

    @FXML
    private Label errorLabel;

    @FXML
    private TextField login;

    @FXML
    private MenuButton levelSelect;

    @FXML
    private Button start;

    private CandyCrushGame game;
    private Validator validator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validator = Validator.getInstance();

        try {
            if (game == null) {
                game = new CandyCrushGame();
                errorLabel.setText("Loading from database ...");
                Task<Integer> loader = loadLevelsFromDb();
                loader.setOnSucceeded(event -> setItems());
                new Thread(loader, "DB Loader Thread").start();
            } else {
                game.getManager().reset();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
    
    public void setGame(CandyCrushGame game) {
        this.game = game;
    }

    private Task<Integer> loadLevelsFromDb() {
        return new Task<Integer>() {

            @Override
            protected Integer call() throws Exception {
                game.initLevels();
                return game.getLevels().size();
            }
        };
    }
    //CHECKSTYLE:ON

    /**
     * Sets the {@code MenuItem} items of the select field, based on the game object's levels.
     */
    public void setItems() {
        game.getLevels().forEach(level -> {
            MenuItem item = new MenuItem("" + level.getID());
            item.setOnAction(e -> levelSelect.setText(item.getText()));
            levelSelect.getItems().add(item);
        });
        errorLabel.setText("Good luck and have fun!");
    }

    /**
     * Starts the game on the selected level.
     *
     * <p>
     * This method only starts the game if the user has selected a level to play on, and given they
     * username. In other cases the user sees an error message on the {@code errorLabel}.</p>
     *
     * <p>
     * The starting of the game includes registering the username in the database and setting the
     * game object's current level to the one selected by the user (based on the id).</p>
     *
     * @param event the click event
     */
    @FXML
    public void startGame(ActionEvent event) {
        if (game.getLevels() == null || game.getLevels().isEmpty()) {
            errorLabel.setText("Loading from database ...");
            return;
        }

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

    //CHECKSTYLE:OFF
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
    //CHECKSTYLE:ON
}
