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
    private Game game;

    private String suggestedArea;
    private String[] selectedCandies;

    /**
     * Constructs a manager that helps keeping consistency between a game and a grid.
     *
     * @param grid the grid with {@code Button} nodes
     * @param game the game with the level and the information about the player
     */
    public GridManager(GridPane grid, Game game) {
        this.grid = grid;
        this.game = game;

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
                suggestedArea = game.getCurrentLevel().getManager().getAvailableMoves();
                showSuggestionMarkers(Level.createCoordinates(suggestedArea));
            }
        }, Main.HELP_INTERVAL, Main.HELP_INTERVAL);
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

    public void setMainGridDimensions() {
        setMainGridDimensions(game.getCurrentLevel());
    }

    public void firstRender() {
        firstRender(game.getCurrentLevel().getBoardState());
    }

    public void hideSuggestionMarkers() {
        if (suggestedArea.equals("") || suggestedArea == null) {
            return;
        }
        LOGGER.debug("Hiding help markers");
        hideSuggestionMarkers(Level.createCoordinates(suggestedArea));
    }

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
    
    public String getSelectedCandies() {
        return selectedCandies[0] + ";" + selectedCandies[1];
    }
    
    public void eraseSelectedCandies(Integer[][] coors) {
        getNode(coors[0][0], coors[0][1]).getStyleClass().removeAll("selected");
        getNode(coors[1][0], coors[1][1]).getStyleClass().removeAll("selected");

        selectedCandies[0] = null;
        selectedCandies[1] = null;
    }
    
    public boolean isSwapReady() {
        return selectedCandies[0] != null && selectedCandies[1] != null;
    }

    /**
     * Creates a number of n*n new columns and rows for the main grid of the {@code Scene}, where n
     * is the size of the given {@code Level}'s board.
     *
     * @param level the {@code Level} instance from which we get the board size.
     */
    public void setMainGridDimensions(Level level) {
        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                grid.getColumnConstraints().add(new ColumnConstraints());
            }
            grid.getRowConstraints().add(new RowConstraints());
        }
    }

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

    public void enableButtonClicks(EventHandler<? super MouseEvent> value) {
        grid.getChildren().stream().forEach(b -> b.setOnMouseClicked(value));
    }

    public void disableButtonClicks() {
        grid.getChildren().stream().forEach(b -> b.setOnMouseClicked(null));
    }

    public Node getNode(int i, int j) {
        return grid.getChildren().get(i * game.getCurrentLevel().getBoardSize() + j);
    }

    public void showSuggestionMarkers(Integer[][] coordinates) {
        for (Integer[] coor : coordinates) {
            getNode(coor[0], coor[1]).getStyleClass().add("highlighted");
        }
    }

    public void hideSuggestionMarkers(Integer[][] coordinates) {
        for (Integer[] coor : coordinates) {
            getNode(coor[0], coor[1]).getStyleClass().removeAll("highlighted");
        }
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
