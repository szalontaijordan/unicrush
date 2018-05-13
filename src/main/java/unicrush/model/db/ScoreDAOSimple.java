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
 * Class implementing the {@code ScoreDAO} interface.
 *
 * <p>
 * This class utilizes an {@code EntityManager} to do the work with the database</p>
 *
 * @author Szalontai Jordán
 */
public class ScoreDAOSimple implements ScoreDAO {

    //CHECKSTYLE:OFF
    private EntityManager em;
    //CHECKSTYLE:ON

    /**
     * Constructs a data access object that utilizes the {@code EntityManager} class.
     *
     * @param em the manager for the entities
     */
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
