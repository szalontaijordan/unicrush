package unicrush.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for a Level entity.
 *
 * @author Szalontai Jordán
 */
@Entity
@Table(name = "UC_LEVEL")
public class LevelEntity {

    @Id
    @Column(name = "id")
    private int levelId;

    @Column(name = "boardSize")
    private int boardSize;

    @Column(name = "walls")
    private String walls;

    @Column(name = "score")
    private int scoreToComplete;

    @Column(name = "steps")
    private int availableSteps;

    public LevelEntity() {
    }

    public LevelEntity(int id, int boardSize, String walls, int scoreToComplete, int availableSteps) {
        this.levelId = id;
        this.boardSize = boardSize;
        this.walls = walls;
        this.scoreToComplete = scoreToComplete;
        this.availableSteps = availableSteps;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String getWalls() {
        return walls;
    }

    public int getScoreToComplete() {
        return scoreToComplete;
    }

    public int getAvailableSteps() {
        return availableSteps;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setWalls(String walls) {
        this.walls = walls;
    }

    public void setScoreToComplete(int scoreToComplete) {
        this.scoreToComplete = scoreToComplete;
    }

    public void setAvailableSteps(int availableSteps) {
        this.availableSteps = availableSteps;
    }
}
