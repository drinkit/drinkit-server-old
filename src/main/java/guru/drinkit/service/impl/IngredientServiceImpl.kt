package guru.drinkit.service.impl

import guru.drinkit.domain.Ingredient
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.IngredientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

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
            throw IngredientConstraintException(count)
        }
        ingredientRepository.delete(id)
        removeBarItems(id)
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Found recipes with this ingredient")
    class IngredientConstraintException(val size: Int) :
            RuntimeException("Ingredient could not be deleted. Used in $size recipes")

    /**
     * @return count of users with removed bar items
     */
    private fun removeBarItems(ingredientId: Int): Int {
        val userList = userRepository.findByUserBarIngredientId(ingredientId)
        for (user in userList) {
            userRepository.removeBarItem(user.username, ingredientId)
        }
        return userList.size
    }

}
