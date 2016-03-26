package guru.drinkit.repository;

import guru.drinkit.domain.UserRecipeStats;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;

public interface RecipesStatisticsRepository extends Repository<UserRecipeStats, ObjectId> {

    UserRecipeStats findOneByRecipeId(Integer id);
}