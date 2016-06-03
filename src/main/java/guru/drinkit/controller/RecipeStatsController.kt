package guru.drinkit.controller

import guru.drinkit.controller.RecipeStatsController.Companion.RESOURCE_NAME
import guru.drinkit.service.StatsService
import guru.drinkit.service.getUser
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
        const val RESOURCE_NAME = "users/me/recipeStats"
    }


    @RequestMapping(method = arrayOf(RequestMethod.PATCH), value = "/{recipeId}/views", params = arrayOf("inc=1"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun incrementViewsCount(@PathVariable recipeId: Int) {
        val userName = getUser()?.username
//        when {
//            jsonNode.get("\$inc")?.get("views")?.intValue() == 1 -> {
                statsService.addViewToRecipe(userName, recipeId)
//            }

//            getUser()?.authorities.contains() jsonNode.get("liked")?.isBoolean == true ->
//                statsService.changeLikes(userName?: throw ForbiddenActionException(), recipeId, jsonNode.get("liked").booleanValue())

//            else -> throw BadRequestException()
        }

    }


//}

//class PatchOperation {
//    data class Increment(val value:Int)
//    data class Update()
//}

