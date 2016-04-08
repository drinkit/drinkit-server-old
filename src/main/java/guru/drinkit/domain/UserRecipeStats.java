package guru.drinkit.domain;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Created by pkolmykov on 12/10/2014.
 */
@SuppressWarnings("UnusedDeclaration")
public class UserRecipeStats {

    private ObjectId id;
    private final String userId;
    private final int recipeId;
    private final int views;
    private final Date lastViewed;

    public UserRecipeStats(int recipeId, String userId) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.views = 1;
        this.lastViewed = new Date();
    }

    @PersistenceConstructor
    public UserRecipeStats(String userId, int recipeId, int views, Date lastViewed) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.views = views;
        this.lastViewed = lastViewed;
    }

    public String getUserId() {
        return userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getViews() {
        return views;
    }

    public Date getLastViewed() {
        return lastViewed;
    }
}
