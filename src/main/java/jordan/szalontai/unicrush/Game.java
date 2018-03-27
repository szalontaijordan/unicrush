package jordan.szalontai.unicrush;

/**
 *
 * @author Szalontai Jordán
 */
public interface Game {

    public Level getCurrentLevel();
    
    public void initLevels() throws Exception;

    public void startCurrentLevel() throws Exception;

    public void addToScore(long plus);

    public long getPlayerScore();
}
