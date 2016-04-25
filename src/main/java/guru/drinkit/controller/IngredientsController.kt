package guru.drinkit.controller

import guru.drinkit.common.DrinkitUtils
import guru.drinkit.domain.Ingredient
import guru.drinkit.service.IngredientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("ingredients")
class IngredientsController @Autowired constructor(
        private var ingredientService: IngredientService
) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun getIngredients(): List<Ingredient> = ingredientService.ingredients

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    fun addNewIngredient(@RequestBody ingredient: Ingredient) {
        Assert.isNull(ingredient.id)
        DrinkitUtils.logOperation("Creating ingredient", ingredient)
        ingredientService.save(ingredient)
    }

    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.PUT))
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Updated")
    fun editIngredient(@RequestBody ingredient: Ingredient, @PathVariable id: Int) {
        DrinkitUtils.assertEqualsIds(ingredient.id!!, id)
        DrinkitUtils.logOperation("Updating ingredient", ingredient)
        ingredientService.save(ingredient)
    }

    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.DELETE))
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Deleted")
    fun delete(@PathVariable id: Int) {
        DrinkitUtils.logOperation("Deleting ingredient", id)
        ingredientService.delete(id)

    }

}
