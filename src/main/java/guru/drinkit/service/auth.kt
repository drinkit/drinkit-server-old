package guru.drinkit.service

import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import org.apache.commons.collections4.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

fun getRolesByAccessLevel(accessLevel: Int): List<BasicUserDetailsService.Role> {
    val roles = ArrayList(EnumSet.allOf(BasicUserDetailsService.Role::class.java))
    CollectionUtils.filter(roles) { role -> role.accessLevel >= accessLevel }
    return roles
}

/**
 * Retrieve userId from security context

 * @return 'ANONYMOUS' for anonymous, 'username' if user authorized and is not admin, otherwise return null;
 */
fun getUserNameAndId(): SimpleUser {
    val authentication = SecurityContextHolder.getContext().authentication
    if (authentication != null) {
        if (authentication.authorities.size == 0 && authentication.principal is BasicUserDetailsService.ExtendedUser) {
            val user = authentication.principal as BasicUserDetailsService.ExtendedUser
            return SimpleUser(user.userId, user.username)
        }
    }
    return SimpleUser(null, User.ANONYMOUS_USER_NAME)
}

data class SimpleUser(val id: String?, val name: String)

@Component
class BasicUserDetailsService @Autowired constructor(
        private val userRepository: UserRepository
) : UserDetailsService {

    enum class Role private constructor(val accessLevel: Int) {

        ROLE_ADMIN(0), ROLE_USER(9);

    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Test msg")
        return ExtendedUser(user)
    }

    class ExtendedUser(val userId: String, username: String?, password: String?, authorities: Collection<GrantedAuthority>) :
            org.springframework.security.core.userdetails.User(username, password, authorities) {


        companion object {
            operator fun invoke(user: User): ExtendedUser {
                return ExtendedUser(user.id!!, user.username, user.password, getAuthorities(user.accessLevel))
            }

            private fun getAuthorities(accessLevel: Int): Collection<GrantedAuthority> =
                    CollectionUtils.collect<Role, SimpleGrantedAuthority>(
                            getRolesByAccessLevel(accessLevel), { role -> SimpleGrantedAuthority(role.name) })
        }


    }

    fun createUser(user: User): Boolean {
        val isNew = userRepository.findByUsername(user.username) == null
        if (isNew) {
            userRepository.save(user)
        }
        return isNew
    }


}
