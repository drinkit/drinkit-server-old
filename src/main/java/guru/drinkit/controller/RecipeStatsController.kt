package guru.drinkit.controller

import guru.drinkit.controller.RecipeStatsController.Companion.RESOURCE_NAME
import guru.drinkit.service.StatsService
import guru.drinkit.service.getUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


/**
 * Created by pkolmykov on 12/12/2014.
 */
@Controller
@RequestMapping(value = RESOURCE_NAME)
open class RecipeStatsController @Autowired constructor(
        val statsService: StatsService

) {

    companion object {
        const val RESOURCE_NAME = "users/me/recipeStats"
    }


    @RequestMapping(method = arrayOf(RequestMethod.PATCH), value = "/{recipeId}/views", params = arrayOf("inc=1"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open fun incrementViewsCount(@PathVariable recipeId: Int) {
        val userName = getUser()?.username
        statsService.addViewToRecipe(userName, recipeId)
    }


    @RequestMapping(method = arrayOf(RequestMethod.PATCH), value = "/{recipeId}/liked", params = arrayOf("value"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    open fun changeLike(@PathVariable recipeId: Int, @RequestParam value: Boolean) {
        val userName = getUser()?.username
        statsService.changeLike(userName ?: throw ForbiddenActionException(), recipeId, value)
    }


}