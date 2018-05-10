package unicrush.model.db;

import java.util.List;

/**
 *
 * @author Szalontai Jordán
 */
public interface ScoreDAO {
    
    public void create(int userId, int levelId, int score);
    
    public void update(int userId, int levelId, int score);
    
    public ScoreEntity find(int userId, int levelId);
    
    public List<ScoreEntity> findAll();
}
