package unicrush.model.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Class implementing the {@code UserDAO} interface.
 *
 * <p>
 * This class utilizes an {@code EntityManager} to do the work with the database</p>
 *
 * @author Szalontai Jordán
 */
public class UserDAOSimple implements UserDAO {

    //CHECKSTYLE:OFF
    private EntityManager em;
    //CHECKSTYLE:ON

    /**
     * Constructs a data access object that utilizes the {@code EntityManager} class.
     *
     * @param em the manager for the entities
     */
    public UserDAOSimple(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(String username) {
        int id;

        do {
            id = (int) (Math.random() * 1000);
        } while (get(id) != null);

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

    @Override
    public UserEntity get(int id) {
        return em.find(UserEntity.class, id);
    }

}
