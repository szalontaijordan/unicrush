package unicrush.model.db;

import java.util.List;

/**
 * Interface describing actions that can be done with level entities.
 *
 * @author Szalontai Jordán
 */
public interface LevelDAO {

    /**
     * Returns a plain old java object that represents an entity in a database.
     *
     * @param id the id field of the database entity
     * @return an object representing a level entity in the database
     */
    public LevelPOJO findLevel(int id);

    /**
     * Returns all the level entities that can be found in the database.
     *
     * @return a list containing level entities
     */
    public List<LevelPOJO> findAll();

    /**
     * Creates a new level entity in the database with fields based on the given parameters.
     *
     * @param size the size of the board
     * @param walls the template string that represents coordinates where walls should be
     * @param score the score required to complete this level
     * @param steps the amount of steps required to complete this level
     */
    public void create(int id, int size, String walls, int score, int steps);
}
