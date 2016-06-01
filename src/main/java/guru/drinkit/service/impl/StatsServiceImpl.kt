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

    private val ANONYMOUS_USER_ID = "anonymous"

    override fun changeLikes(userId: String, recipeId: Int, liked: Boolean) {
        userRepository.changeRecipeLike(userId, recipeId, liked)
        recipeRepository.adjustLikesCount(recipeId, if (liked) 1 else -1)
    }

    override fun addViewToRecipe(userId: String?, recipeId: Int) {
        userRepository.incrementRecipeViews(userId ?: ANONYMOUS_USER_ID, recipeId)
        recipeRepository.incrementViews(recipeId)
    }

}