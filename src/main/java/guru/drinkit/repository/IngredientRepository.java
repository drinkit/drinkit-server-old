package guru.drinkit.repository;

import guru.drinkit.domain.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pkolmykov on 12/8/2014.
 */
public interface IngredientRepository extends MongoRepository<Ingredient, Integer> {

    Ingredient findFirstByOrderByIdDesc();

}
