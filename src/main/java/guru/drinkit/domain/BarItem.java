package guru.drinkit.domain;

import org.bson.types.ObjectId;

/**
 * Created by pkolmykov on 2/8/2015.
 */
public class BarItem {
    private ObjectId userId;
    private Integer ingredientId;
    private boolean isActive;

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(final ObjectId userId) {
        this.userId = userId;
    }

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

    public static
}
