package unicrush.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Szalontai Jordán
 */
@Entity
@Table(name = "UC_USER")
public class UserEntity {
    
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "username")
    private String username;
    
    public UserEntity() {
    }

    public UserEntity(int id,String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }    
}
