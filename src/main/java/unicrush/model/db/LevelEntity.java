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
import javax.persistence.Table;

/**
 * Class for a Level entity.
 *
 * @author Szalontai Jordán
 */
@Entity
@Table(name = "UC_LEVEL")
public class LevelEntity {

    //CHECKSTYLE:OFF
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
    //CHECKSTYLE:ON
}
