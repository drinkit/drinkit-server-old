package guru.drinkit.service.impl

import guru.drinkit.common.ExtendedUser
import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class BasicUserDetailsService @Autowired constructor(
        private val userRepository: UserRepository
) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Test msg")
        return ExtendedUser(user)
    }

    fun createUser(user: User): Boolean {
        val isNew = userRepository.findByUsername(user.username) == null
        if (isNew) {
            userRepository.save(user)
        }
        return isNew
    }


}
