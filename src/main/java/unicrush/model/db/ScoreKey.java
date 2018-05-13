package unicrush.model.db;

import java.io.Serializable;

/**
 * Class representing a composite primary key for the {@link ScoreEntity} class.
 *
 * @author Szalontai Jordán
 */
public class ScoreKey implements Serializable {

    //CHECKSTYLE:OFF
    protected int userId;
    protected int levelId;

    public ScoreKey() {
    }

    public ScoreKey(int userId, int levelId) {
        this.userId = userId;
        this.levelId = levelId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.userId;
        hash = 29 * hash + this.levelId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScoreKey other = (ScoreKey) obj;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.levelId != other.levelId) {
            return false;
        }
        return true;
    }
    //CHECKSTYLE:ON
}
