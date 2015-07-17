package guru.drinkit.repository;

import guru.drinkit.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, UserBarRepository {

    User findByUsername(String username);
}
