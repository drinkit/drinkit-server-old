package guru.drinkit.repository;

import java.util.List;

import guru.drinkit.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, UserBarRepository {

    User findByUsername(String username);

    @Query("{barItems : {$elemMatch : {ingredientId : ?0}}}")
    List<User> findByUserBarIngredientId(Integer id);
}
