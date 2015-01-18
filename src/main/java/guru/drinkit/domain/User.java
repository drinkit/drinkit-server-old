package guru.drinkit.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Created by pkolmykov on 12/8/2014.
 */
public class User {

    public static final int ACCESS_LVL_USER = 9;
    public static final int ACCESS_LVL_ADMIN = 0;

    private ObjectId id;
    private String username;
    private String password;
    private String displayName;
    private Integer accessLevel;

    public User() {
    }

    @PersistenceConstructor
    protected User(ObjectId id, String username, String password, String displayName, Integer accessLevel) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.accessLevel = accessLevel;
    }

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }
}
