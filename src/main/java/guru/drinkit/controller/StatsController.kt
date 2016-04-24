package guru.drinkit.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

const val RESOURCE_NAME = "/stats"

/**
 * Created by pkolmykov on 12/12/2014.
 */
@Controller
@RequestMapping(value = RESOURCE_NAME)
class StatsController {



    @RequestMapping(method = arrayOf(RequestMethod.PATCH), value = "/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun incrementViewsCount(@PathVariable recipeId: Int) {
    }


}
