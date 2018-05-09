package unicrush.model.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Szalontai Jordán
 */
public class UserDAOSimple implements UserDAO {

    private EntityManager em;

    public UserDAOSimple(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(String username) {
        int id;
        
        do {
            id = (int) (Math.random() * 1000);
        } while(em.find(UserEntity.class, id) != null);
        
        em.getTransaction().begin();
        em.persist(new UserEntity(id, username));
        em.getTransaction().commit();
    }

    @Override
    public List<UserEntity> findByName(String username) {
        TypedQuery<UserEntity> q = em.createQuery("SELECT u FROM UserEntity u WHERE u.username='"
                + username + "'", UserEntity.class);

        return q.getResultList();
    }

}
