package unicrush.model.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Class implementing the {@code LevelDAO} interface.
 *
 * <p>
 * This class utilizes an {@code EntityManager} to do the work with the database</p>
 *
 * @author Szalontai Jordán
 */
public class SimpleLevelDAO implements LevelDAO {

    private EntityManager em;

    /**
     * Creates a data access object that utilizes the {@code EntityManager} class.
     *
     * @param em the manager for the entities
     */
    public SimpleLevelDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public LevelPOJO findLevel(int id) {
        return em.find(LevelPOJO.class, id);
    }

    @Override
    public List<LevelPOJO> findAll() {
        TypedQuery<LevelPOJO> query
                = em.createQuery("SELECT l FROM LevelPOJO l ORDER BY l.levelId", LevelPOJO.class);

        return query.getResultList();
    }

    @Override
    public void create(int id, int size, String walls, int score, int steps) {
        em.getTransaction().begin();
        em.persist(new LevelPOJO(id, size, walls, score, steps));
        em.getTransaction().commit();
    }

}
