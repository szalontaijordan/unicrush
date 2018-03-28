package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 * Class for building a {@code StandardLevel}.
 *
 * This class is a subclass of {@code LevelBuilder}
 *
 * @author Szalontai Jordán
 */
public class StandardLevelBuilder extends LevelBuilder {

    /**
     * Constructs a {@code StandardLevelBuilder} that has a new
     * {@code StandardLevel} field which we build.
     */
    public StandardLevelBuilder() {
        super();
        this.level = new StandardLevel();
    }

    /**
     * Constructs a {@code StandardLevelBuilder} that has a copy of an existing
     * {@code StandardLevel} field which we continue building.
     *
     * @param l the {@code Level} which we continue building
     */
    public StandardLevelBuilder(Level l) {
        super(l);
    }

    /**
     * Fills up the board with random {@code Candy} instances and the given
     * walls.
     *
     * @return {@code this} so we can chain the methods after each other
     * @throws Exception if something unexpected happens, e.g. the walls are not
     * set
     */
    @Override
    public LevelBuilder fillBoard() throws Exception {
        if (level.getBoardSize() == 0) {
            throw new Exception("Board size must be set first!");
        }

        if (level.getWalls() == null) {
            throw new Exception("Walls must be set first!");
        }

        if (level.getBoard() == null) {
            level.setBoard(new Candy[level.getBoardSize()][level.getBoardSize()]);
        }

        for (int i = 0; i < level.getBoardSize(); i++) {
            for (int j = 0; j < level.getBoardSize(); j++) {
                level.set(i, j, new Candy(Candy.getRandomColorState()));
            }
        }

        Arrays.stream(level.getWalls())
                .forEach(wall -> level.set(wall[0], wall[1], null));

        return this;
    }
}
