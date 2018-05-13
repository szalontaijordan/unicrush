package unicrush.model.db;

import java.util.List;

/**
 * Interface describing actions that can be done with score entities.
 *
 * @author Szalontai Jordán
 */
public interface ScoreDAO {

    /**
     * Creates a new score entity in the database with fields based on the given parameters.
     *
     * @param userId the id of the user
     * @param levelId the id of the level
     * @param score the score of the user on the level they played
     */
    public void create(int userId, int levelId, int score);

    /**
     * Updates the numerical score field of score entity in the database with fields based on the
     * level id and the user id.
     *
     * @param userId the id of the user
     * @param levelId the id of the level
     * @param score the score of the user on the level they played
     */
    public void update(int userId, int levelId, int score);

    /**
     * Returns a plain old java object that represents a score entity in the database, identified by
     * two fields.
     *
     * @param userId the id of the user (first component of the entity's primary key)
     * @param levelId the id of the level (second component of the entity's primary key)
     * @return an object representing a score entity in the database
     */
    public ScoreEntity find(int userId, int levelId);

    /**
     * Returns all the score entities that can be found in the database.
     *
     * @return a list containing score entities
     */
    public List<ScoreEntity> findAll();
}
