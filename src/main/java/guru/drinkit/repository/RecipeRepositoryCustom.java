package guru.drinkit.repository;

import guru.drinkit.common.Criteria;
import guru.drinkit.domain.Recipe;

import java.util.List;

/**
 * Created by pkolmykov on 12/8/2014.
 */
public interface RecipeRepositoryCustom {
    List<Recipe> findByCriteria(Criteria criteria);
}
