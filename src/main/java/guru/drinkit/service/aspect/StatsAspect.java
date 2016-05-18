package guru.drinkit.service.aspect;

import java.util.Date;

import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.domain.UserRecipeStats;
import guru.drinkit.repository.RecipeRepository;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by pkolmykov on 12/10/2014.
 */
@Component
@Aspect
public class StatsAspect {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private RecipeRepository recipeRepository;

    @After(value = "@annotation(EnableStats) && args(id)")
    public void writeStats(int id) {
        recipeRepository.incrementViews(id);
        String userName = DrinkitUtils.getUserName();
        if (userName != null) {
            mongoOperations.upsert(query(
                            where("recipeId").is(id).and("userId").is(userName)),
                    new Update().inc("views", 1).set("lastViewed", new Date()), UserRecipeStats.class);
        }
    }


}

