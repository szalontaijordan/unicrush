package unicrush.model.db;

import javax.persistence.EntityManager;

/**
 *
 * @author Szalontai Jordán
 */
public class ScoreDAOSimple implements ScoreDAO {

    private EntityManager em;
    
    public ScoreDAOSimple(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void create(int userId, int levelId, int score) {
        em.getTransaction().begin();
        em.persist(new ScoreEntity(userId, levelId, score));
        em.getTransaction().commit();
    }

    @Override
    public void update(int userId, int levelId, int score) {
        // TODO
    }

    @Override
    public void find(int userId, int levelId) {
        // TODO
    }

    @Override
    public void findAll() {
        // TODO
    }
    
}
