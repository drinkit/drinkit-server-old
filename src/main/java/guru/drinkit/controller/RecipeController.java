package guru.drinkit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.drinkit.common.Criteria;
import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.domain.Recipe;
import guru.drinkit.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.List;

import static guru.drinkit.controller.RecipeController.RESOURCE_NAME;

@Controller
@RequestMapping(value = RESOURCE_NAME)
public class RecipeController {

    public static final String RESOURCE_NAME = "recipes";

    @Autowired
    RecipeService recipeService;

    @RequestMapping(value = "/{recipeId}", method = RequestMethod.GET)
    @ResponseBody
    public Recipe getRecipeById(@PathVariable int recipeId) {
        return recipeService.findById(recipeId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@RequestBody Recipe recipe) {
        Assert.isNull(recipe.getId());
        DrinkitUtils.logOperation("Creating recipe", recipe);
        recipeService.save(recipe);
    }


    @RequestMapping(method = RequestMethod.GET, params = "criteria")
    @ResponseBody
    public List<Recipe> searchRecipes(@RequestParam(value = "criteria", required = false) Criteria criteria) {
//        List<Recipe> recipeDtos;
//        if (json != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Criteria criteria;
//            try {
//                criteria = objectMapper.readValue(json, Criteria.class);
//            } catch (IOException e) {
//                LOGGER.error("Bad criteria", e);
//                return null;
//            }
//            recipeDtos = recipeService.findByCriteria(criteria);
//        } else {
//            recipeDtos = recipeService.findAll();
//        }
//        return recipeDtos;
        return criteria == null ? recipeService.findAll() : recipeService.findByCriteria(criteria);
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Criteria.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(new ObjectMapper().readValue(text, Criteria.class));
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {
        if (recipeService.findById(id) != null) {
            DrinkitUtils.logOperation("Deleting recipe", id);
            recipeService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "{recipeId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable int recipeId, @RequestBody Recipe recipe) {
        Assert.isTrue(recipeId == recipe.getId(), "id from uri and id from json should be identical");
        DrinkitUtils.logOperation("Updating recipe", recipe);
        recipeService.save(recipe);
    }

    @RequestMapping(method = RequestMethod.GET, params = "namePart")
    @ResponseBody
    public List<Recipe> findRecipesByNamePart(@RequestParam() String namePart) {
        return recipeService.findByRecipeNameContaining(namePart);
    }

    @RequestMapping(value = "{recipeId}/media", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadMedia(@RequestBody String json, @PathVariable int recipeId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(json);
        byte[] image = objectMapper.convertValue(root.get("image"), byte[].class);
        byte[] thumbnail = objectMapper.convertValue(root.get("thumbnail"), byte[].class);
        recipeService.saveMedia(recipeId, image, thumbnail);
    }


}
