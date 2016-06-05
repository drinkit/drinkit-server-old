package guru.drinkit.service

import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


/**
 * Retrieve userId from security context

 * @return 'ANONYMOUS' for anonymous, 'username' if user authorized and is not admin, otherwise return null;
 */
fun getUsername(): String? = (SecurityContextHolder.getContext().authentication?.principal
        as? org.springframework.security.core.userdetails.User)?.username

fun getUser(): org.springframework.security.core.userdetails.User? = (SecurityContextHolder.getContext().authentication?.principal
        as? org.springframework.security.core.userdetails.User)

@Component
class BasicUserDetailsService @Autowired constructor(
        private val userRepository: UserRepository
) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Test msg")
        return org.springframework.security.core.userdetails.User(user.username, user.password, getAuthorities(user.role))
    }

//    private fun getAuthorities(role: AuthorityRole) : Collection<GrantedAuthority> =
//            CollectionUtils.collect<Role, SimpleGrantedAuthority>(
//                    role.getAccumulatedRoles(), { role -> SimpleGrantedAuthority(role.name) })

    private fun getAuthorities(authorityRole: User.AuthorityRole): Collection<GrantedAuthority> =
            User.AuthorityRole.values()
                    .filter { role -> role.ordinal < authorityRole.ordinal }
                    .map { role -> SimpleGrantedAuthority("ROLE_${role.name}") }

    fun createUser(user: User): Boolean {
        val isNew = userRepository.findByUsername(user.username) == null
        if (isNew) {
            userRepository.save(user)
        }
        return isNew
    }


}
