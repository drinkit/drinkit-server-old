package guru.drinkit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import guru.drinkit.common.DrinkitUtils
import guru.drinkit.controller.RecipeController.Companion.RESOURCE_NAME
import guru.drinkit.domain.Recipe
import guru.drinkit.service.Criteria
import guru.drinkit.service.RecipeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*
import java.beans.PropertyEditorSupport
import java.io.IOException
import javax.validation.Valid

/**
 * @author pkolmykov
 */

@Controller
@RequestMapping(value = RESOURCE_NAME)
open class RecipeController @Autowired constructor(
        val recipeService: RecipeService,
        val objectMapper: ObjectMapper) {

    companion object {
        const val RESOURCE_NAME = "recipes"
    }

    @RequestMapping(value = "/{recipeId}", method = arrayOf(GET))
    @ResponseBody
    open fun getRecipeById(@PathVariable recipeId: Int): Recipe {
        return recipeService.findSafe(recipeId)
    }

    @RequestMapping(method = arrayOf(POST))
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun createRecipe(@RequestBody recipe: Recipe): Recipe {
        DrinkitUtils.logOperation("Creating recipe", recipe)
        recipeService.insert(recipe)
        return recipe
    }


    @RequestMapping(method = arrayOf(GET), params = arrayOf("criteria"))
    @ResponseBody
    open fun searchRecipes(@RequestParam(value = "criteria", required = false) criteria: Criteria?): List<Recipe> {
        return if (criteria == null) recipeService.findAll() else recipeService.findByCriteria(criteria)
    }

    @InitBinder
    open fun initBinder(webDataBinder: WebDataBinder) {
        webDataBinder.registerCustomEditor(Criteria::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String) {
                value = try {
                    objectMapper.readValue(text, Criteria::class.java)
                } catch (e: IOException) {
                    throw IllegalArgumentException(e)
                }
            }
        })
    }

    @RequestMapping(value = "{recipeId}", method = arrayOf(DELETE))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun deleteRecipe(@PathVariable recipeId: Int) {
        DrinkitUtils.logOperation("Deleting recipe", recipeId)
        recipeService.delete(recipeId)
    }

    @RequestMapping(value = "{recipeId}", method = arrayOf(PUT))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun updateRecipe(@PathVariable recipeId: Int, @RequestBody @Valid recipe: Recipe) {
        DrinkitUtils.logOperation("Updating recipe", recipe)
        recipeService.update(recipeId, recipe)
    }

    @RequestMapping(method = arrayOf(GET), params = arrayOf("namePart"))
    @ResponseBody
    open fun findRecipesByNamePart(@RequestParam namePart: String): List<Recipe> {
        return recipeService.findByRecipeNameContaining(namePart)
    }

    @RequestMapping(value = "{recipeId}/media", method = arrayOf(POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    open fun uploadMedia(@RequestBody json: String, @PathVariable recipeId: Int) {
        val objectMapper = ObjectMapper()
        val root = objectMapper.readTree(json)
        val image = objectMapper.convertValue(root.get("image"), ByteArray::class.java)
        val thumbnail = objectMapper.convertValue(root.get("thumbnail"), ByteArray::class.java)
        if (image == null || thumbnail == null) {
            throw IllegalArgumentException("image AND thumbnail is required")
        }
        recipeService.saveMedia(recipeId, image, thumbnail)
    }

}
