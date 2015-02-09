package guru.drinkit.repository;

import guru.drinkit.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, ObjectId>, UserBarRepository {

    User findByUsername(String username);
}
