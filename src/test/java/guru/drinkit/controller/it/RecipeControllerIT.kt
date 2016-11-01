package guru.drinkit.controller.it

import guru.drinkit.controller.common.AbstractRestMockMvc
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.service.RecipeService
import org.apache.commons.io.IOUtils
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class RecipeControllerIT : AbstractRestMockMvc() {


    @Autowired
    internal lateinit var recipeService: RecipeService

    @Autowired
    internal lateinit var recipeRepository: RecipeRepository


    private lateinit var insertedRecipe: Recipe

    @Before
    fun insertTestRecipe() {
        val newRecipeDto = createNewRecipeDto()
        insertedRecipe = recipeService.insert(newRecipeDto)
    }


    @Test
    @Ignore
    @Throws(Exception::class)
    fun testGetRecipeByIdWithStats() {
        //        val recipeId = insertedRecipe.id
        //        mockMvc.perform(get(RESOURCE_ENDPOINT + "/" + recipeId))
        //                .andExpect(status().isOk())
        //                .andExpect(content().json(objectMapper.writeValueAsString(insertedRecipe)));
        //        assertEquals(recipesStatisticsRepository.findOneByRecipeId
        //                (recipeId).getViews(), recipesStatisticsRepository.findOneByRecipeId(recipeId).getViews());
        //        int views = recipesStatisticsRepository.findOneByRecipeId
        //                (recipeId).getViews();
        //        SecurityContextHolder.createEmptyContext();
        //        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(null, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
        //        mockMvc.perform(get(RESOURCE_ENDPOINT + "/" + recipeId))
        //                .andExpect(status().isOk())
        //                .andExpect(content().json(objectMapper.writeValueAsString(insertedRecipe)));
        //        RecipeStatistics statistics = recipesStatisticsRepository.findOneByRecipeId(recipeId);
        //        assertNotNull(statistics.getLastViewed());
        //        assertEquals(1, statistics.getViews());

    }

    @Test
    @Throws(Exception::class)
    fun testCreateRecipe() {
        val newRecipeDto = createNewRecipeDto()
        newRecipeDto.name = "new name"
        //        newRecipeDto.setRecipeStats(new Recipe.RecipeStats(2,123,123));
        mockMvc.perform(
                post(RESOURCE_ENDPOINT).content(objectMapper.writeValueAsBytes(newRecipeDto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated)
        //                .andExpect(jsonPath("$.id").exists())
        //                .andExpect(jsonPath("$.name").value(createNewRecipeDto().getName()));
        //        val recipe = recipeService.find(2)
        //        assertThat(recipe.getName()).isEqualToIgnoringCase("new name");
    }

    @Test
    @Throws(Exception::class)
    fun testSearchRecipes() {

    }

    @Test
    @Throws(Exception::class)
    fun testDeleteRecipe() {
        assertNotNull(recipeRepository.findOne(insertedRecipe.id!!))
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + insertedRecipe.id)).andExpect(status().isNoContent)
        assertNull(recipeRepository.findOne(insertedRecipe.id!!))
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + insertedRecipe.id!!)).andExpect(status().isNotFound)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateRecipe() {
        insertedRecipe.ingredientsWithQuantities = listOf(Recipe.IngredientWithQuantity(firstIngredient.id!!, 13, null))
        insertedRecipe.name = "modified"
        mockMvc.perform(
                put(RESOURCE_ENDPOINT + "/" + insertedRecipe.id!!).content(objectMapper.writeValueAsBytes(insertedRecipe)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent)
        val actualRecipe = recipeService.find(insertedRecipe.id!!)

        assertEquals(insertedRecipe, actualRecipe)                       //fail
    }

    @Test
    @Throws(Exception::class)
    fun testUploadMedia() {
        val image = IOUtils.toByteArray(javaClass.classLoader.getResourceAsStream("test.jpg"))
        val objectNode = objectMapper.createObjectNode()
        objectNode.put("image", image)
        objectNode.put("thumbnail", image)

        mockMvc.perform(post(RESOURCE_ENDPOINT + "/" + insertedRecipe.id + "/media").contentType(MediaType.APPLICATION_JSON).content(objectNode.toString())).andExpect(status().isNoContent)

        val recipe = recipeService.find(insertedRecipe.id!!)!!
        assertNotNull(recipe.imageUrl)
        assertNotNull(recipe.thumbnailUrl)
    }

    @Test
    @Throws(Exception::class)
    fun testFindRecipesByNamePart() {
        mockMvc.perform(get(RESOURCE_ENDPOINT).param("namePart", "Integration Tests")).andExpect(status().isOk).andExpect(content().json(objectMapper.writeValueAsString(listOf<Recipe>(insertedRecipe))))

        mockMvc.perform(get(RESOURCE_ENDPOINT).param("namePart", "%%%%%%%not exist$$$$$$$")).andExpect(status().isOk).andExpect(jsonPath("$", hasSize<Any>(0)))
    }

    companion object {

        private val RESOURCE_ENDPOINT = "/recipes"
        val VIEWS = 419
    }
}