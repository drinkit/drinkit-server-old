package guru.drinkit.controller

import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.repository.UserRepository
import guru.drinkit.security.Role
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author pkolmykov
 */
class RecipeStatsControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Test
    fun incrementViewsCount() {
        verifyAccess({
            MockMvcRequestBuilders.patch(RecipeStatsController.RESOURCE_NAME + "/1/views")
                    .param("inc", "1")
                    .contentType(MediaType.APPLICATION_JSON)
        }, MockMvcResultMatchers.status().isNoContent
//                RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("inc").description("=1")
        )
        Mockito.verify(userRepository, Mockito.times(3)).incrementRecipeViews(Matchers.anyString(), Matchers.eq(1))
        Mockito.verify(recipeRepository, Mockito.times(3)).incrementViews(1)
    }

    @Test
    fun changeLikes() {
        verifyAccess({
            MockMvcRequestBuilders.patch(RecipeStatsController.RESOURCE_NAME + "/1")
                    .content("{\"liked\" : true}")
                    .contentType(MediaType.APPLICATION_JSON)
        }, MockMvcResultMatchers.status().isNoContent, Role.ADMIN, Role.USER)
        Mockito.verify(userRepository, Mockito.times(2)).changeRecipeLike(Matchers.anyString(), Matchers.eq(1), Matchers.eq(true))
        Mockito.verify(recipeRepository, Mockito.times(2)).adjustLikesCount(1, 1)
    }

}