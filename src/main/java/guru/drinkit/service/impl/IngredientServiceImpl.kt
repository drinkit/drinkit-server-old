package guru.drinkit.service.impl

import guru.drinkit.domain.Ingredient
import guru.drinkit.exception.RecipesFoundException
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.IngredientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class IngredientServiceImpl @Autowired constructor(
        val ingredientRepository: IngredientRepository,
        val recipeRepository: RecipeRepository,
        val userRepository: UserRepository
) : IngredientService, CrudServiceImpl<Int, Ingredient>(ingredientRepository) {


    override fun insert(entity: Ingredient): Ingredient {
        val lastRecipeId = ingredientRepository.findFirstByOrderByIdDesc()?.id ?: 0
        entity.id = lastRecipeId + 1;
        return ingredientRepository.save(entity)
    }

    override fun delete(id: Int) {
        findSafe(id)

        val count = recipeRepository.countByIngredientId(id)
        if (count === 0) {
            ingredientRepository.delete(id)
        } else {
            throw RecipesFoundException(count)
        }
        ingredientRepository.delete(id)
        removeBarItems(id)
    }

    /**
     * @return count of users with removed bar items
     */
    private fun removeBarItems(ingredientId: Int): Int {
        val userList = userRepository.findByUserBarIngredientId(ingredientId)
        for (user in userList) {
            userRepository.removeBarItem(user.id!!, ingredientId)
        }
        return userList.size
    }

}
