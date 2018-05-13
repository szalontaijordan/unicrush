package unicrush.model.db;

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
