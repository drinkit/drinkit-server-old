package guru.drinkit.repository;

import java.util.List;

import guru.drinkit.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RecipeRepository extends MongoRepository<Recipe, Integer>, RecipeRepositoryCustom {

    List<Recipe> findByNameContainingIgnoreCase(String namePart);

    Recipe findFirstByOrderByIdDesc();

    @Query(value = "{ingredientsWithQuantities : {$elemMatch : {ingredientId : ?0}}}", count = true)
    Integer countByIngredientId(Integer id);

}
