package jordan.szalontai.unicrush;

import java.util.Arrays;

public class StandardLevelBuilder extends LevelBuilder {
    
    public StandardLevelBuilder() {
        super();
        this.l = new StandardLevel();
    }
    
    public StandardLevelBuilder(Level l) {
        super(l);
    }
    
    @Override
    public LevelBuilder fillBoard() throws Exception {
        if (l.getBoardSize() == 0) {
            throw new Exception("Board size must be set first!");
        }

        if (l.getWalls() == null) {
            throw new Exception("Walls must be set first!");
        }

        if (l.getBoard() == null) {
            l.setBoard(new Candy[l.getBoardSize()][l.getBoardSize()]);
        }

        for (int i = 0; i < l.getBoardSize(); i++) {
            for (int j = 0; j < l.getBoardSize(); j++) {
                l.set(i, j, new Candy(Candy.getRandomColorState()));
            }
        }

        Arrays.stream(l.getWalls())
                .forEach(wall -> l.set(wall[0], wall[1], null));

        return this;
    }
}
