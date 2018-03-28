package jordan.szalontai.unicrush;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class starting the JavaFx application.
 *
 * @author Szalontai Jordán
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/UniCrush.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("UniCrush");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gives the URL of the specified image.
     *
     * @param candy the name of the image file without the extension
     * @return a value representing the path of the image given, that can be
     * used in CSS
     */
    public static String getCandyImageURL(String candy) {
        return "url('/candy/" + candy + ".png')";
    }

    /**
     * This method is only required to call {@code launch(args} so the
     * JavaFx application can start.
     *
     * This might be used as a fallback method if the application fails to
     * start.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
