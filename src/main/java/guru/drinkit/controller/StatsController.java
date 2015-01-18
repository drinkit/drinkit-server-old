package guru.drinkit.controller;

import guru.drinkit.service.aspect.EnableStats;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static guru.drinkit.controller.StatsController.RESOURCE_NAME;

/**
 * Created by pkolmykov on 12/12/2014.
 */
@Controller
@RequestMapping(value = RESOURCE_NAME)
public class StatsController {

    public static final String RESOURCE_NAME = "stats";

    @SuppressWarnings("UnusedParameters")
    @RequestMapping(method = RequestMethod.PATCH, value = "{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @EnableStats
    public void incrementViewsCount(@PathVariable int recipeId) {
    }

}
