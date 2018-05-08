package unicrush.model.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class for creating data access objects for a level entity.
 *
 * @author Szalontai Jordán
 */
public class LevelDAOFactory implements AutoCloseable {

    private static LevelDAOFactory instance;

    private static EntityManager entityManager;
    private static EntityManagerFactory factory;

    static {
        instance = new LevelDAOFactory();
        factory = Persistence.createEntityManagerFactory("UNICRUSH");
        entityManager = factory.createEntityManager();
    }

    private LevelDAOFactory() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static LevelDAOFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new data access object for a level entity.
     * 
     * @return an object that defines actions for a level entity
     */
    public LevelDAO createLevelDAO() {
        return new SimpleLevelDAO(entityManager);
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
        factory.close();
    }
}
