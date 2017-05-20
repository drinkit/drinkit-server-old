package guru.drinkit.controller


import cz.jiripinkas.jsitemapgenerator.ImageBuilder
import cz.jiripinkas.jsitemapgenerator.WebPage
import cz.jiripinkas.jsitemapgenerator.WebPageBuilder
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Created by Crabar on 14.05.2016.
 */
@RestController
@RequestMapping("sitemap")
open class SitemapController
@Autowired
constructor(private val recipeRepository: RecipeRepository,
            private val ingredientRepository: IngredientRepository) {


    @GetMapping(produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    open fun getSitemap(): String {
        val sitemapGenerator = SitemapGenerator("https://drinkit.guru", arrayOf(SitemapGenerator.AdditionalNamespace.IMAGE))
        sitemapGenerator.addPage(WebPageBuilder().build())
        sitemapGenerator.addPage(WebPageBuilder().name("bar").build())
        sitemapGenerator.addPages(generateRecipesPages())
        sitemapGenerator.addPages(generateIngredientsPages())
        return sitemapGenerator.constructSitemapString()
    }

    private fun generateRecipesPages() = recipeRepository.findAll()
            .filter(Recipe::published)
            .map { convertRecipe(it) }


    private fun convertRecipe(recipe: Recipe): WebPage {
        val recipePage = WebPageBuilder().name("recipes/" + recipe.id.toString()).build()
        recipePage.addImage(ImageBuilder().loc("https://prod-drunkedguru.rhcloud.com" + recipe.imageUrl)
                .license("https://creativecommons.org/licenses/by-nc-nd/4.0/")
                .build())
        return recipePage
    }

    private fun generateIngredientsPages() = ingredientRepository.findAll()
            .map(Ingredient::id)
            .map { WebPageBuilder().name("ingredients/" + it.toString()).build() }

}
