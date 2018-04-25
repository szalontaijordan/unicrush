/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
