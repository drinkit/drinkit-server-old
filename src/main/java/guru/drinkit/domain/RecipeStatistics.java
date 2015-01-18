package guru.drinkit.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

/**
 * Created by pkolmykov on 12/10/2014.
 */
@SuppressWarnings("UnusedDeclaration")
public class RecipeStatistics {

    private ObjectId id;
    private String userId;
    private int recipeId;
    private int views;
    private Date lastViewed;

    public RecipeStatistics(int recipeId, String userId) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.views = 1;
        this.lastViewed = new Date();
    }

    @PersistenceConstructor
    public RecipeStatistics(String userId, int recipeId, int views, Date lastViewed) {
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
