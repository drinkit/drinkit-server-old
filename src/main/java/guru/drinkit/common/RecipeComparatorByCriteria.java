package guru.drinkit.common;

import guru.drinkit.domain.Recipe;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeComparatorByCriteria implements Comparator<Recipe> {

    private final Criteria criteria;

    public RecipeComparatorByCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public int compare(Recipe recipe1, Recipe recipe2) {
        int result = getOverlapRate(recipe2) - getOverlapRate(recipe1);
        if (result == 0) {
            result = recipe1.getCocktailIngredients().length - recipe2.getCocktailIngredients().length;
        }
        return result;
    }

    private int getOverlapRate(Recipe recipe) {
        Set<Integer> tmp = new HashSet<>(criteria.getIngredients());
        tmp.retainAll(Arrays.stream(recipe.getCocktailIngredients())
                .map(val -> val[0]).collect(Collectors.toSet()));
        return tmp.size();
    }
}
