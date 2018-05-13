package unicrush.model.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class for creating data access objects for a level entity.
 *
 * @author Szalontai Jordán
 */
public class DAOFactory implements AutoCloseable {

    //CHECKSTYLE:OFF
    private static DAOFactory instance;

    private static EntityManager entityManager;
    private static EntityManagerFactory factory;
    
    static {
        instance = new DAOFactory();
        factory = Persistence.createEntityManagerFactory("UNICRUSH");
        entityManager = factory.createEntityManager();
    }
    
    private DAOFactory() {
    }
    //CHECKSTYLE:ON

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static DAOFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new data access object for level entities.
     *
     * @return an object that defines actions for level entities
     */
    public LevelDAO createLevelDAO() {
        return new LevelDAOSimple(entityManager);
    }

    /**
     * Creates a new data access object for user entities.
     *
     * @return an object that defines actions for user entities
     */
    public UserDAO createUserDAO() {
        return new UserDAOSimple(entityManager);
    }

    /**
     * Creates a new data access object for score entities.
     *
     * @return an object that defines actions for score entities
     */
    public ScoreDAO createScoreDAO() {
        return new ScoreDAOSimple(entityManager);
    }

    /**
     * Closes the {@code EntityManager} and the {@code EntityManagerFactory} objects.
     *
     * @throws Exception if an error occurs during the closing process
     */
    @Override
    public void close() throws Exception {
        entityManager.close();
        factory.close();
    }
}
