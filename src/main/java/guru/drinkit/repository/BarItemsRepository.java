package guru.drinkit.repository;

import java.util.List;

import guru.drinkit.domain.BarItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pkolmykov on 2/8/2015.
 */
public interface BarItemsRepository extends MongoRepository<BarItem, ObjectId> {

    List<BarItem> findByUserId()

}
