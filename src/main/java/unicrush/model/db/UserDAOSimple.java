package unicrush.model.db;

/*-
 * #%L
 * unicrush
 * %%
 * Copyright (C) 2018 Faculty of Informatics
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
