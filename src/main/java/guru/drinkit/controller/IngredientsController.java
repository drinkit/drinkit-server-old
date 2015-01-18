package guru.drinkit.controller;

import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("ingredients")
public class IngredientsController {

    @Autowired
    IngredientService ingredientService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewIngredient(@RequestBody Ingredient ingredient) {
        Assert.isNull(ingredient.getId());
        DrinkitUtils.logOperation("Creating ingredient", ingredient);
        ingredientService.save(ingredient);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Updated")
    public void editIngredient(@RequestBody Ingredient ingredient, @PathVariable int id) {
        Assert.isTrue(id == ingredient.getId(), "id from uri and id from json should be identical");
        DrinkitUtils.logOperation("Updating ingredient", ingredient);
        ingredientService.save(ingredient);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Deleted")
    public void delete(@PathVariable int id) {
        DrinkitUtils.logOperation("Deleting ingredient", id);
        ingredientService.delete(id);
    }

}
