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
public class LevelDAOSimple implements LevelDAO {

    //CHECKSTYLE:OFF
    private EntityManager em;
    //CHECKSTYLE:ON
    
    /**
     * Constructs a data access object that utilizes the {@code EntityManager} class.
     *
     * @param em the manager for the entities
     */
    public LevelDAOSimple(EntityManager em) {
        this.em = em;
    }

    @Override
    public LevelEntity findLevel(int id) {
        return em.find(LevelEntity.class, id);
    }

    @Override
    public List<LevelEntity> findAll() {
        TypedQuery<LevelEntity> query
                = em.createQuery("SELECT l FROM LevelEntity l ORDER BY l.levelId", LevelEntity.class);

        return query.getResultList();
    }

    @Override
    public void create(int id, int size, String walls, int score, int steps) {
        em.getTransaction().begin();
        em.persist(new LevelEntity(id, size, walls, score, steps));
        em.getTransaction().commit();
    }

}
