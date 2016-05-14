package guru.drinkit.controller;


import cz.jiripinkas.jsitemapgenerator.WebPageBuilder;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;
import guru.drinkit.domain.Recipe;
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

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getSitemap() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("https://drinkit.guru");
        // const pages
        sitemapGenerator.addPage(new WebPageBuilder().build());
        sitemapGenerator.addPage(new WebPageBuilder().name("bar").build());
        // recipes
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            sitemapGenerator.addPage(new WebPageBuilder().name("recipes/" + recipe.getId().toString()).build());
        }
        return sitemapGenerator.constructSitemapString();
    }
}
