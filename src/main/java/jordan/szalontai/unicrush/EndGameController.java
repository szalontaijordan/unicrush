package jordan.szalontai.unicrush;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class.
 *
 * @author szalontaijordan
 */
public class EndGameController implements Initializable {

    @FXML
    private Label gratText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setGrat(String text) {
        this.gratText.setText(text);
    }
    
}
