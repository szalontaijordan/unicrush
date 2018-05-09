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

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static DAOFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new data access object for a level entity.
     * 
     * @return an object that defines actions for a level entity
     */
    public LevelDAO createLevelDAO() {
        return new LevelDAOSimple(entityManager);
    }
    
    public UserDAO createUserDAO() {
        return new UserDAOSimple(entityManager);
    }

    public ScoreDAO createScoreDAO() {
        return new ScoreDAOSimple(entityManager);
    }
    
    @Override
    public void close() throws Exception {
        entityManager.close();
        factory.close();
    }
}
