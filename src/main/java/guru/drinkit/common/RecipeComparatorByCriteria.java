package guru.drinkit.common;

import java.util.Collection;
import java.util.Comparator;

import guru.drinkit.domain.Recipe;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.ArrayIterator;

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
            result = recipe2.getCocktailIngredients().length - recipe1.getCocktailIngredients().length;
        }
        return result;
    }

    private int getNotMatchesIngredientsCount(Recipe recipe) {

        Collection<Integer> ingredientsIdsFromRecipe = collect(new ArrayIterator<Integer[]>(recipe.getCocktailIngredients()), new Transformer<Integer[], Integer>() {
            @Override
            public Integer transform(final Integer[] integers) {
                return integers[0];
            }
        });
        ingredientsIdsFromRecipe.removeAll(criteria.getIngredients());
        return ingredientsIdsFromRecipe.size();
    }
}
