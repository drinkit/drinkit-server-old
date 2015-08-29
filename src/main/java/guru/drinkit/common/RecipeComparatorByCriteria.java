package guru.drinkit.common;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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
        int result = getOverlapRate(recipe2) - getOverlapRate(recipe1);
        if (result == 0) {
            result = recipe1.getCocktailIngredients().length - recipe2.getCocktailIngredients().length;
        }
        return result;
    }

    private int getOverlapRate(Recipe recipe) {
        Set<Integer> tmp = new HashSet<>(criteria.getIngredients());

        tmp.removeAll(collect(new ArrayIterator<Integer[]>(recipe.getCocktailIngredients()), new Transformer<Integer[], Integer>() {
            @Override
            public Integer transform(final Integer[] integers) {
                return integers[0];
            }
        }));
        return tmp.size();
    }
}
