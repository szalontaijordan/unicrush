/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unicrush.controller;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import unicrush.model.Level;

/**
 *
 * @author szalontaijordan
 */
public interface GridManager {
    
    /**
     * Creates a number of n*n new columns and rows for the main grid of the {@code Scene}, where n
     * is the size of the given {@code Level}'s board.
     *
     * @param level the {@code Level} instance from which we get the board size.
     */
    public void setMainGridDimensions(Level level);
    
    public void firstRender(String boardState);
    
    public void renderBoardState(String boardState);
    
    public Node getNode(int i, int j);
    
    public void enableButtonClicks(EventHandler<? super MouseEvent> value);
    
    public void disableButtonClicks();
    
    public default void showSuggestionMarkers(Integer[][] coordinates) {
        for (Integer[] coor : coordinates) {
            getNode(coor[0], coor[1]).getStyleClass().add("highlighted");
        }
    }

    public default void hideSuggestionMarkers(Integer[][] coordinates) {
        for (Integer[] coor : coordinates) {
            getNode(coor[0], coor[1]).getStyleClass().removeAll("highlighted");
        }
    }
}
