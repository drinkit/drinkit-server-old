package guru.drinkit.controller

import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */
//todo
class UserControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun principal() {
        mockMvc.perform(get(UserController.RESOURCE_NAME + "/principal")).andExpect(status().isUnauthorized)
    }

    @Test
    fun currentUserUnauthorized() {
        mockMvc.perform(get(UserController.RESOURCE_NAME + "/me")).andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "test user")
    fun currentUserAuthorized() {
        Mockito.`when`(userRepository.findByUsername("test user")).thenReturn(User())
        mockMvc.perform(get(UserController.RESOURCE_NAME + "/me")).andExpect(status().isOk)
    }

    @Test
    fun registerUser() {

    }

}