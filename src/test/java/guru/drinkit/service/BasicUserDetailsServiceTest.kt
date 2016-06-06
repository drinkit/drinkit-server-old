package guru.drinkit.service

import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.iterable.Extractor
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority

/**
 * @author pkolmykov
 */
class BasicUserDetailsServiceTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userDetailsService: BasicUserDetailsService

    private val USERNAME = "name"

    @Test
    fun loadUserByUsernameRoleUser() {
        Mockito.`when`(userRepository.findByUsername(USERNAME)).thenReturn(User(username = USERNAME, password = "pswrd", role = User.AuthorityRole.USER))
        val userDetails = userDetailsService.loadUserByUsername(USERNAME)
        Assertions.assertThat(userDetails.authorities).extracting(Extractor<GrantedAuthority, String> { it -> it.authority })
                .containsOnly("ROLE_USER")
    }

    @Test
    fun loadUserByUsernameRoleAdmin() {
        Mockito.`when`(userRepository.findByUsername(USERNAME)).thenReturn(User(username = USERNAME, password = "pswrd", role = User.AuthorityRole.ADMIN))
        val userDetails = userDetailsService.loadUserByUsername(USERNAME)
        Assertions.assertThat(userDetails.authorities).extracting(Extractor<GrantedAuthority, String> { it -> it.authority })
                .containsOnly("ROLE_USER", "ROLE_ADMIN")
    }

    @Test
    fun createUser() {

    }

}