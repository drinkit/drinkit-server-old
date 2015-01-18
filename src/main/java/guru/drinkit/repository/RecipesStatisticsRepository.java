package guru.drinkit.repository;

import guru.drinkit.domain.RecipeStatistics;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;

public interface RecipesStatisticsRepository extends Repository<RecipeStatistics, ObjectId> {

    RecipeStatistics findOneByRecipeId(Integer id);
}