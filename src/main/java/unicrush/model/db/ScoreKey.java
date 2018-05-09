package unicrush.model.db;

import java.io.Serializable;

/**
 *
 * @author Szalontai Jordán
 */
public class ScoreKey implements Serializable {

    protected int userId;
    protected int levelId;

    public ScoreKey() {
    }

    public ScoreKey(int userId, int levelId) {
        this.userId = userId;
        this.levelId = levelId;
    }

    @Override
    public boolean equals(Object obj) {
        ScoreKey that = (ScoreKey) obj;
        return obj instanceof ScoreKey && that.userId == this.userId && that.userId == this.userId;
    }

    @Override
    public int hashCode() {
        return 7 + 13 * Integer.hashCode(userId) + 13 * Integer.hashCode(levelId);
    }

}
