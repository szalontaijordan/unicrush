package jordan.szalontai.unicrush;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public final class GridManipulator {

    private GridPane grid;

    public GridManipulator(GridPane grid) {
        this.grid = grid;
    }

    public void createGridFromLevel(Level level) {
        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                grid.getColumnConstraints().add(new ColumnConstraints());
            }
            grid.getRowConstraints().add(new RowConstraints());
        }
    }

}
