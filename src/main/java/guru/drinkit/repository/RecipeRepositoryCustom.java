package guru.drinkit.repository;

import java.util.List;

import guru.drinkit.common.Criteria;
import guru.drinkit.domain.Recipe;

/**
 * Created by pkolmykov on 12/8/2014.
 */
public interface RecipeRepositoryCustom {
    List<Recipe> findByCriteria(Criteria criteria);

    void incrementLikes(Integer recipeId);

    void decrementLikes(Integer recipeId);

    void incrementViews(Integer recipeId);

    boolean update(Recipe recipe);
}
