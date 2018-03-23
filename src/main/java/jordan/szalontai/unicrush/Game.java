package jordan.szalontai.unicrush;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Szalontai Jordán
 */
public class Game extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Game.class.getResource("/fxml/UniCrush.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");
        
        stage.setTitle("UniCrush");
        stage.setScene(scene);
        stage.show();
    }
    
    public static String getCandyImageURL(String candy) {
        return "url('/candy/" + candy + ".png')";
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
