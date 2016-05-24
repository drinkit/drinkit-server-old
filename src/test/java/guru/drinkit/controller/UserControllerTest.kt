package guru.drinkit.controller

import guru.drinkit.controller.common.AbstractMockMvcTest
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */
//todo
class UserControllerTest : AbstractMockMvcTest() {


    @Test
    fun principal() {
        mockMvc.perform(get(UserController.RESOURCE_NAME + "/principal")).andExpect(status().isUnauthorized)
    }

    @Test
    fun currentUser() {
        mockMvc.perform(get(UserController.RESOURCE_NAME + "/me")).andExpect(status().isUnauthorized)
    }

    @Test
    fun registerUser() {

    }

}