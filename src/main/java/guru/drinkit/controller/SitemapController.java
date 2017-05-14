package guru.drinkit.controller;


import cz.jiripinkas.jsitemapgenerator.ImageBuilder;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.WebPageBuilder;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.domain.Recipe;
import guru.drinkit.repository.IngredientRepository;
import guru.drinkit.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by Crabar on 14.05.2016.
 */
@Controller
@RequestMapping("sitemap")
public class SitemapController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getSitemap() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("https://drinkit.guru", new SitemapGenerator.AdditionalNamespace[]{SitemapGenerator.AdditionalNamespace.IMAGE});
        // const pages
        sitemapGenerator.addPage(new WebPageBuilder().build());
        sitemapGenerator.addPage(new WebPageBuilder().name("bar").build());
        // recipes
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            if (recipe.getPublished()) {
                WebPage recipePage = new WebPageBuilder().name("recipes/" + recipe.getId().toString()).build();
                recipePage.addImage(new ImageBuilder().loc("https://prod-drunkedguru.rhcloud.com" + recipe.getImageUrl())
                        .license("https://creativecommons.org/licenses/by-nc-nd/4.0/")
                        .build());
                sitemapGenerator.addPage(recipePage);
            }
        }
        // ingredients
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient ingredient : ingredients) {
            WebPage ingredientPage = new WebPageBuilder().name("ingredients/" + ingredient.getId().toString()).build();
            sitemapGenerator.addPage(ingredientPage);
        }

        return sitemapGenerator.constructSitemapString();
    }
}
