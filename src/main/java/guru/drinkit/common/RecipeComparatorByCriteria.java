package guru.drinkit.common;

import java.util.Collection;
import java.util.Comparator;

import guru.drinkit.domain.Recipe;
import guru.drinkit.service.Criteria;
import org.apache.commons.collections4.Transformer;

import static org.apache.commons.collections4.CollectionUtils.collect;

public class RecipeComparatorByCriteria implements Comparator<Recipe> {

    private final Criteria criteria;

    public RecipeComparatorByCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public int compare(Recipe recipe1, Recipe recipe2) {
        int result = getNotMatchesIngredientsCount(recipe1) - getNotMatchesIngredientsCount(recipe2);
        if (result == 0) {
            result = recipe2.getIngredientsWithQuantities().size() - recipe1.getIngredientsWithQuantities().size();
        }
        return result;
    }

    private int getNotMatchesIngredientsCount(Recipe recipe) {
        Collection<Integer> ingredientsIdsFromRecipe = collect(recipe.getIngredientsWithQuantities(), new Transformer<Recipe.IngredientWithQuantity, Integer>() {
            @Override
            public Integer transform(final Recipe.IngredientWithQuantity ingredientWithQuantity) {
                return ingredientWithQuantity.getIngredientId();
            }
        });
        ingredientsIdsFromRecipe.removeAll(criteria.getIngredients());
        return ingredientsIdsFromRecipe.size();
    }
}
