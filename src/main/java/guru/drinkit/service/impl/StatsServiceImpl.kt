package guru.drinkit.service.impl

import guru.drinkit.repository.RecipeRepository
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.StatsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author pkolmykov
 */
@Service
class StatsServiceImpl @Autowired constructor(
        private val recipeRepository: RecipeRepository,
        private val userRepository: UserRepository
) : StatsService {

    override fun addViewToRecipe(recipeId: Int, userId: String) {
        userRepository.incrementRecipeViews(userId, recipeId)
        recipeRepository.incrementViews(recipeId)
    }

}