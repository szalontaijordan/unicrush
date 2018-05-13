package unicrush.model.db;

import java.util.List;

/**
 * Interface describing actions that can be done with user entities.
 *
 * @author Szalontai Jordán
 */
public interface UserDAO {

    /**
     * Creates a new user entity in the database with fields based on the parameters.
     *
     * @param username the name of the new user
     */
    public void create(String username);

    /**
     * Returns a plain old java object that represents an user entity in a database.
     *
     * @param id the id field of the entity
     * @return an object representing an user entity in the database
     */
    public UserEntity get(int id);

    /**
     * Returns all user entities from the database, that have the same name as the given parameter.
     *
     * @param username the name we look for
     * @return a list containing objects representing user entities in the database
     */
    public List<UserEntity> findByName(String username);
}
