package guru.drinkit.domain;

import java.util.List;

/**
 * Created by pkolmykov on 12/8/2014.
 */
@SuppressWarnings("unused")
public class User {

    public static final int ACCESS_LVL_USER = 9;
    public static final int ACCESS_LVL_ADMIN = 0;

    private String id;
    private String username;
    private String password;
    private String displayName;
    private Integer accessLevel;
    private List<BarItem> barItems;

    public List<Integer> getLikes() {
        return likes;
    }

    public void setLikes(List<Integer> likes) {
        this.likes = likes;
    }

    private List<Integer> likes;

    public List<BarItem> getBarItems() {
        return barItems;
    }

    public void setBarItems(final List<BarItem> barItems) {
        this.barItems = barItems;
    }

    //    public User() {
//    }
//
//    @PersistenceConstructor
//    protected User(ObjectId id, String username, String password, String displayName, Integer accessLevel) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.displayName = displayName;
//        this.accessLevel = accessLevel;
//    }

    public String getId() {
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

    /**
     * @author pkolmykov
     */
    public static class BarItem {

        public BarItem() {
        }

        public BarItem(final Integer ingredientId) {
            this.ingredientId = ingredientId;
        }

        private Integer ingredientId;
        private boolean isActive = true;

        public Integer getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(final Integer ingredientId) {
            this.ingredientId = ingredientId;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(final boolean isActive) {
            this.isActive = isActive;
        }

    }
}
