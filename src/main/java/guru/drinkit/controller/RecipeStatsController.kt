package guru.drinkit.controller

import guru.drinkit.common.DrinkitUtils
import guru.drinkit.controller.RecipeStatsController.Companion.RESOURCE_NAME
import guru.drinkit.service.StatsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus


/**
 * Created by pkolmykov on 12/12/2014.
 */
@Controller
@RequestMapping(value = RESOURCE_NAME)
class RecipeStatsController @Autowired constructor(
        private val statsService: StatsService

) {

    companion object {
        const val RESOURCE_NAME = "user/me/recipeStats"
    }


    @RequestMapping(method = arrayOf(RequestMethod.PATCH), value = "/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun incrementViewsCount(@PathVariable recipeId: Int) {
        val userId = DrinkitUtils.getUserNameAndId().id!!
        statsService.addViewToRecipe(recipeId, userId)
    }


}