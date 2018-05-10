package unicrush.model.db;

import java.util.List;

/**
 *
 * @author Szalontai Jordán
 */
public interface UserDAO {
    
    public void create(String username);
    
    public UserEntity get(int id);
    
    public List<UserEntity> findByName(String username);
}
