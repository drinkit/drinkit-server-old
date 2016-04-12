package guru.drinkit.service

import guru.drinkit.common.Criteria
import guru.drinkit.domain.Recipe
import org.springframework.security.access.prepost.PreAuthorize

interface RecipeService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun insert(recipe: Recipe): Recipe

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun update(recipe: Recipe): Recipe

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delete(id: Int)

    fun findAll(): List<Recipe>

    fun findByCriteria(criteria: Criteria): List<Recipe>

    fun findByRecipeNameContaining(namePart: String): List<Recipe>

    fun findById(id: Int): Recipe?

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun saveMedia(recipeId: Int, image: ByteArray?, thumbnail: ByteArray?)
}
