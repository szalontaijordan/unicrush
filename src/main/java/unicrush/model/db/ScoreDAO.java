package unicrush.model.db;

/**
 *
 * @author Szalontai Jordán
 */
public interface ScoreDAO {
    
    public void create(int userId, int levelId, int score);
    
    public void update(int userId, int levelId, int score);
    
    public void find(int userId, int levelId);
    
    public void findAll();
}
