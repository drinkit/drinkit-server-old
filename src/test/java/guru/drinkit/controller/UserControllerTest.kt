package guru.drinkit.controller

import guru.drinkit.controller.common.AbstractMockMvcTest
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */
class UserControllerTest : AbstractMockMvcTest() {


    @Test
    fun principal() {
        verifyAccess(httpMethod = HttpMethod.GET,
                uri = UserController.RESOURCE_NAME + "/principal",
                resultMatcher = status().isOk)
    }
//
//    @Test
//    fun currentUser() {
//        MockMvcRequestBuilders.get(UserController.RESOURCE_NAME + "/me")
//    }

    @Test
    fun registerUser() {

    }

}