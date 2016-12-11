package guru.drinkit.controller

import guru.drinkit.controller.IngredientController.Companion.RESOURCE_NAME
import guru.drinkit.domain.Ingredient
import guru.drinkit.dto.SuggestedIngredientDto
import guru.drinkit.service.IngredientService
import guru.drinkit.service.SuggestingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * @author pkolmykov
 */
@Controller
@RequestMapping(RESOURCE_NAME)
open class IngredientController {

    @Autowired
    open lateinit var ingredientService: IngredientService
    @Autowired
    open lateinit var suggestingService: SuggestingService

    companion object {
        const val RESOURCE_NAME = "ingredients"
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    open fun getIngredients(): List<Ingredient> =
            ingredientService.findAll()

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun addNewIngredient(@RequestBody ingredient: Ingredient) {
        ingredientService.insert(ingredient)
    }


    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.PUT))
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Updated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun editIngredient(@RequestBody ingredient: Ingredient, @PathVariable id: Int) =
            ingredientService.update(id, ingredient)

    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.DELETE))
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun delete(@PathVariable id: Int) =
            ingredientService.delete(id)

    @RequestMapping(path = arrayOf("/suggest"))
    @ResponseBody
    open fun suggestIngredients(
            @RequestParam(name = "id[]") userIngredients: Set<Int>,
            @RequestParam(defaultValue = "true") viewAll: Boolean
    ): List<SuggestedIngredientDto> {
        if (userIngredients.size < 3) return emptyList()
        return suggestingService.suggestIngredients(userIngredients, viewAll)
    }


}
