package guru.drinkit.repository.impl;

import java.util.List;

import guru.drinkit.domain.Recipe;
import guru.drinkit.repository.RecipeRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.Repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by pkolmykov on 12/8/2014.
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "UnusedDeclaration"})
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Autowired
    private Repositories repositories;

    @Override
    public List<Recipe> findByCriteria(guru.drinkit.common.Criteria criteria) {
        Criteria mongoCriteria = new Criteria();
        if (criteria.getIngredients().size() > 0) {
            mongoCriteria.and("ingredientsWithQuantities").elemMatch(where("ingredientId").in(criteria.getIngredients()));
        }
        if (criteria.getOptions().size() > 0) {
            mongoCriteria.and("options").all(criteria.getOptions());
        }
        if (criteria.getCocktailTypes().size() > 0) {
            mongoCriteria.and("cocktailTypeId").in(criteria.getCocktailTypes());
        }

        return operations.find(query(mongoCriteria), Recipe.class);
    }

    @Override
    public void incrementLikes(Integer recipeId) {
        changeLikesCount(recipeId, 1);
    }

    @Override
    public void decrementLikes(Integer recipeId) {
        changeLikesCount(recipeId, -1);
    }

    @Override
    public void incrementViews(Integer recipeId) {
//        operations.findAndModify(query(where("id").is(recipeId)), new Update().inc("views", 1), Recipe.class);
    }

    @Override
    public boolean update(final Recipe recipe) {
        Recipe originalRecipe = operations.findById(recipe.getId(), Recipe.class);
        repositories.getEntityInformationFor(Recipe.class);
        return false;
//        operations.updateFirst()
    }

    private void changeLikesCount(Integer id, Integer likesOffset) {
        operations.findAndModify(query(where("id").is(id)), new Update().inc("stats.likes", likesOffset), Recipe.class);
    }
}
