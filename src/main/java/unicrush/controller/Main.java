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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class starting the JavaFx application.
 *
 * @author Szalontai Jordán
 */
public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static final int HELP_INTERVAL = 30000;
    public static final int POP_INTERVAL = 450;

    /**
     * The names of the existing FXML files.
     */
    public static final String[] SCENES = {
        "GameScene", "EndGameScene"
    };
    
    /**
     * The possible messages that can be displayed if we earn a lot of points.
     */
    public static final String[] MESSAGES = {
        "Sweet", "Delicious", "Divine", "Tasty"
    };

    private static boolean funmode = false;

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Application started!");
        loadNewScene(stage, SCENES[0], "Game Scene");
        LOGGER.info("Switched to root scene");
    }

    /**
     * Switches to the given scene on a stage, with a given title.
     *
     * @param stage the window
     * @param fxmlResName the FXML's name without the extension
     * @param windowText the title of the window
     * @return the {@code FXMLLoader} object that loads the scene into the
     * window
     * @throws IOException when there is no FXML file
     */
    public static FXMLLoader loadNewScene(Stage stage, String fxmlResName, String windowText) throws IOException {
        FXMLLoader fl = new FXMLLoader(Main.class.getResource("/fxml/" + fxmlResName + ".fxml"));
        Parent root = fl.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle(windowText);
        stage.setScene(scene);
        stage.show();

        return fl;
    }

    /**
     * Gives the URL of the specified image.
     *
     * @param candy the name of the image file without the extension
     * @return a value representing the path of the image given, that can be
     * used in CSS
     */
    public static String getCandyImageURL(String candy) {
        if (funmode) {
            return "url('/candy/fun/" + candy + ".png')";
        }
        return "url('/candy/" + candy + ".png')";
    }
    
    /**
     * Gives a random message from the defined messages.
     *
     * @return a random element of {@code MESSAGES}
     */
    public static String getMessage() {
        return MESSAGES[(int) (Math.random() * MESSAGES.length)];
    }

    /**
     * This method is only required to call {@code launch(args} so the JavaFx
     * application can start.
     *
     * This might be used as a fallback method if the application fails to
     * start.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Launching application ...");
        launch(args);
    }
}
