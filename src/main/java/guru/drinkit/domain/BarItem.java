package guru.drinkit.domain;

/**
 * @author pkolmykov
 */
public class BarItem {
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
