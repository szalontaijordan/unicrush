package unicrush.controller;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unicrush.model.Game;

import unicrush.model.Level;

/**
 * Class for keeping the grid and the level consistent.
 *
 * @author szalontaijordan
 */
public class GameGridManager implements GridManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameGridManager.class);

    private Thread popThread;
    private Timer suggestionTimer;

    private GridPane grid;
    private Game game;

    private String suggestedArea;

    public GameGridManager(GridPane grid, Game game) {
        this.grid = grid;
        this.game = game;

        suggestedArea = "";
        suggestionTimer = new Timer("Highlight Thread", true);

        popThread = new Thread("Pop Task Thread");
        popThread.setDaemon(true);
    }

    public Task<Integer> startPopTask(List<String> boardStates, final long add) {
        Task<Integer> popTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return onPopTaskCalled(boardStates);
            }
        };
        popTask.setOnSucceeded(e -> onPopTaskSucceeded());
        popThread = new Thread(popTask);
        popThread.start();
        return popTask;
    }

    public void startSuggestionTask() {
        suggestionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LOGGER.debug("Highlighted coordinates: {}", suggestedArea);
                suggestedArea = SimpleLevelManager.getInstance()
                        .getAvailableMoves(game.getCurrentLevel());
                showSuggestionMarkers(Level.createCoordinates(suggestedArea));
            }
        }, Main.HELP_INTERVAL, Main.HELP_INTERVAL);
    }

    private Integer onPopTaskCalled(List<String> boardStates) {
        int stateIndex = 0;

        while (stateIndex < boardStates.size()) {
            renderBoardState(boardStates.get(stateIndex++));
            try {
                Thread.sleep(Main.POP_INTERVAL);
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return stateIndex;
    }
    
    private void onPopTaskSucceeded() {
        if (suggestedArea.equals("") || suggestedArea == null) {
            return;
        }
        LOGGER.debug("Hiding help markers");
        hideSuggestionMarkers(Level.createCoordinates(suggestedArea));
    }
    
    public void setMainGridDimensions() {
        setMainGridDimensions(game.getCurrentLevel());
    }

    public void firstRender() {
        firstRender(game.getCurrentLevel().getBoardState());
    }
    
    @Override
    public void setMainGridDimensions(Level level) {
        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                grid.getColumnConstraints().add(new ColumnConstraints());
            }
            grid.getRowConstraints().add(new RowConstraints());
        }
    }

    @Override
    public void firstRender(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                Button b = new Button();

                if (!color.equals("x")) {
                    b.setStyle("-fx-background-image:" + Main.getCandyImageURL(color));
                } else {
                    b.setStyle("-fx-background-color: transparent");
                }

                GridPane.setConstraints(b, j, i);
                grid.getChildren().add(b);
            }
        }
    }

    @Override
    public void renderBoardState(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String color = Character.toString(state[i].charAt(j));

                if (!color.equals("x")) {
                    getNode(i, j).setStyle("-fx-background-image: " + Main.getCandyImageURL(color));
                }
            }
        }
    }

    @Override
    public void enableButtonClicks(EventHandler<? super MouseEvent> value) {
        grid.getChildren().stream().forEach(b -> b.setOnMouseClicked(value));
    }

    @Override
    public void disableButtonClicks() {
        grid.getChildren().stream().forEach(b -> b.setOnMouseClicked(null));
    }

    @Override
    public Node getNode(int i, int j) {
        return grid.getChildren().get(i * game.getCurrentLevel().getBoardSize() + j);
    }
    
    public Thread getPopThread() {
        return popThread;
    }

    public Timer getSuggestionTimer() {
        return suggestionTimer;
    }

    public String getSuggestedArea() {
        return suggestedArea;
    }
}
