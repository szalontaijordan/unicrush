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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 *
 * @author Szalontai Jordán
 */
@Entity
@Table(name = "UC_SCORE")
@IdClass(ScoreKey.class)
public class ScoreEntity {
    
    //CHECKSTYLE:OFF
    @Id
    @Column(name = "userId")
    private int userId;
    
    @Id
    @Column(name = "levelId")
    private int levelId;
    
    @Column(name = "score")
    private int score;
    
    public ScoreEntity() {
    }

    public ScoreEntity(int userId, int levelId, int score) {
        this.userId = userId;
        this.levelId = levelId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getScore() {
        return score;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public void setScore(int score) {
        this.score = score;
    }
    //CHECKSTYLE:ON
}
