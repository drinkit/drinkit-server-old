package guru.drinkit.controller

import guru.drinkit.controller.IngredientController.Companion.RESOURCE_NAME
import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.Ingredient
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.security.Role
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.Collections.singletonList

/**
 * @author pkolmykov
 */

@Suppress("SpringKotlinAutowiredMembers")
class IngredientControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var ingredientRepository: IngredientRepository

    @Before
    fun setUp() {
        `when`(ingredientRepository.findAll()).thenReturn(singletonList(Ingredient()))
        `when`(ingredientRepository.findOne(1)).thenReturn(Ingredient(id = 1))
        `when`(ingredientRepository.save(anyObject<Ingredient>())).thenReturn(Ingredient(id = 1))
    }

    @Test
    fun getIngredients() {
        verifyAccess({ MockMvcRequestBuilders.get(RESOURCE_NAME) }, MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun addNewIngredient() {
        val ingredientJson = objectMapper.writeValueAsBytes(Ingredient(name = "ingr", description = "any", vol = 40, category = "cat"))
        verifyAccess({ MockMvcRequestBuilders.post(RESOURCE_NAME).content((ingredientJson)).contentType(MediaType.APPLICATION_JSON) },
                MockMvcResultMatchers.status().isCreated, Role.ADMIN)
    }

    @Test
    fun editIngredient() {
        val ingredientJson = objectMapper.writeValueAsBytes(Ingredient(id = 1, name = "ingr", description = "any", vol = 40, category = "cat"))
        verifyAccess({ MockMvcRequestBuilders.put(RESOURCE_NAME + "/1").content(ingredientJson).contentType(MediaType.APPLICATION_JSON) }, MockMvcResultMatchers.status().isNoContent, Role.ADMIN)
    }

    @Test
    fun delete() {
        verifyAccess({ MockMvcRequestBuilders.delete(RESOURCE_NAME + "/1") },
                MockMvcResultMatchers.status().isNoContent, Role.ADMIN)
    }

}