package unicrush.model;

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
import unicrush.controller.Main;

/**
 * Class for keeping the grid and the level consistent.
 *
 * @author szalontaijordan
 */
public final class GridManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GridManager.class);

    private Thread popThread;
    private Timer suggestionTimer;

    private GridPane grid;
    private CandyCrushGame game;
    private Validator validator;

    private String suggestedArea;
    private String[] selectedCandies;

    /**
     * Constructs a manager that helps keeping consistency between a game and a grid.
     *
     * @param grid the grid with {@code Button} nodes
     * @param game the game with the level and the information about the player
     */
    public GridManager(GridPane grid, CandyCrushGame game) {
        this.grid = grid;
        this.game = game;
        this.validator = new Validator();

        this.suggestedArea = "";
        this.selectedCandies = new String[2];

        this.suggestionTimer = new Timer("Suggestion Timer", true);

        this.popThread = new Thread("Pop Task Thread");
        this.popThread.setDaemon(true);
    }

    /**
     * Starts a JavaFX {@code Task} that renders all board states to the grid with a small delay.
     *
     * <p>
     * When the task is finished it hides the previous suggestion if there was any</p>
     *
     * @param boardStates the list with the given board state strings
     * @return the {@code Task} that started
     *
     * @see #renderAll(java.util.List)
     * @see #hideSuggestionMarkers()
     */
    public Task<Integer> startPopTask(List<String> boardStates) {
        Task<Integer> popTask = new Task<Integer>() {
            @Override
            protected Integer call() {
                return renderAll(boardStates);
            }
        };
        popThread = new Thread(popTask);
        popThread.start();
        return popTask;
    }

    /**
     * Starts a {@code Timer} that will look for available moves, when found the first one,
     * highlights the area in which a move is possible.
     */
    public void startSuggestionTask() {
        suggestionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LOGGER.debug("Highlighted coordinates: {}", suggestedArea);
                suggestedArea = game.getManager().getAvailableMoves();
                showSuggestionMarkers();
            }
        }, Main.HELP_INTERVAL, Main.HELP_INTERVAL);
    }

    /**
     * Sets the main grid constraints and fills it up with new {@code Node} instances with a
     * background of the corresponding image of the candy in the board.
     */
    public void firstRender() {
        setMainGridDimensions();
        String[] state = game.getCurrentLevel().getBoardState().split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                Button b = new Button();
                String url = Main.getCandyImageURL(state[i].charAt(j));

                if (!validator.isEmptyString(url)) {
                    b.setStyle("-fx-background-image:" + url);
                } else {
                    b.setStyle("-fx-background-color: transparent");
                }

                GridPane.setConstraints(b, j, i);
                grid.getChildren().add(b);
            }
        }
    }

    /**
     * Sets the background of all the nodes in the grid to the corresponding image of the candy in
     * the board.
     *
     * @param boardState
     */
    public void renderBoardState(String boardState) {
        String[] state = boardState.split(";");

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length(); j++) {
                String url = Main.getCandyImageURL(state[i].charAt(j));

                if (!validator.isEmptyString(url)) {
                    getNode(i, j).setStyle("-fx-background-image: " + url);
                }
            }
        }
    }

    private Integer renderAll(List<String> boardStates) {
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

    private void setMainGridDimensions() {
        Level level = game.getCurrentLevel();

        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                grid.getColumnConstraints().add(new ColumnConstraints());
            }
            grid.getRowConstraints().add(new RowConstraints());
        }
    }

    /**
     * Adds the {@code highlighted} class to the CSS class list of the {@code Node} instances, so
     * they will appear with a green background.
     */
    public void showSuggestionMarkers() {
        if (!validator.isEmptyString(suggestedArea)) {
            LOGGER.debug("Showing help markers");

            Integer[][] coordinates = Level.createCoordinates(suggestedArea);

            for (Integer[] coor : coordinates) {
                getNode(coor[0], coor[1]).getStyleClass().add("highlighted");
            }
        }
    }

    /**
     * Removes the {@code highlighted} class from the CSS class list of the {@code Node} instances,
     * so they will not appear with a green background.
     */
    public void hideSuggestionMarkers() {
        if (!validator.isEmptyString(suggestedArea)) {
            LOGGER.debug("Hiding help markers");

            Integer[][] coordinates = Level.createCoordinates(suggestedArea);

            for (Integer[] coor : coordinates) {
                getNode(coor[0], coor[1]).getStyleClass().removeAll("highlighted");
            }
        }
    }

    /**
     * Updates the array for tracing the coordinates of the currently selected candies.
     *
     * @param i the row index of the new selection
     * @param j the column index of the new selection
     */
    public void updateSelectedCandies(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (selectedCandies[k] == null) {
                selectedCandies[k] = i + "," + j;
                getNode(i, j).getStyleClass().add("selected");
                break;
            }
            if (selectedCandies[k].equals(i + "," + j)) {
                selectedCandies[k] = null;
                getNode(i, j).getStyleClass().removeAll("selected");
                break;
            }
        }
    }

    /**
     * Returns the coordinates of the selected candies in a template string.
     *
     * @return a template string with the coordinates that were selected (a component can be null)
     */
    public String getSelectedCandies() {
        return selectedCandies[0] + ";" + selectedCandies[1];
    }

    /**
     * Resets the array that keeps track of the coordinates of the selected candies.
     */
    public void eraseSelectedCandies() {
        Integer[][] coors = Level.createCoordinates(getSelectedCandies());

        getNode(coors[0][0], coors[0][1]).getStyleClass().removeAll("selected");
        getNode(coors[1][0], coors[1][1]).getStyleClass().removeAll("selected");

        selectedCandies[0] = null;
        selectedCandies[1] = null;
    }

    /**
     * Sets the same click event listener for all the nodes in the gird.
     *
     * @param handler the handler that handles click events
     */
    public void enableButtonClicks(EventHandler<? super MouseEvent> handler) {
        grid.getChildren().stream().forEach(button -> button.setOnMouseClicked(handler));
    }

    /**
     * Sets the click event listener for all the nodes in the grid to null.
     */
    public void disableButtonClicks() {
        grid.getChildren().stream().forEach(button -> button.setOnMouseClicked(null));
    }

    private Node getNode(int i, int j) {
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
