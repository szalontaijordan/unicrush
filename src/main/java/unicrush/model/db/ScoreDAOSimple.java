package unicrush.model.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
        ScoreEntity se = find(userId, levelId);
        if (score > se.getScore()) {
            em.getTransaction().begin();
            se.setScore(score);
            em.getTransaction().commit();
        }
    }

    @Override
    public ScoreEntity find(int userId, int levelId) {
        return em.find(ScoreEntity.class, new ScoreKey(userId, levelId));
    }

    @Override
    public List<ScoreEntity> findAll() {
        TypedQuery<ScoreEntity> query
                = em.createQuery("SELECT s FROM ScoreEntity s", ScoreEntity.class);

        return query.getResultList();
    }

}
