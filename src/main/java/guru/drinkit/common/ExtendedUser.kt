package guru.drinkit.common

import org.apache.commons.collections4.CollectionUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * @author pkolmykov
 */
class ExtendedUser(val userId: String, username: String?, password: String?, authorities: Collection<GrantedAuthority>) :
        User(username, password, authorities) {


    companion object {
        operator fun invoke(user: guru.drinkit.domain.User): ExtendedUser {
            return ExtendedUser(user.id!!, user.username, user.password, getAuthorities(user.accessLevel))
        }

        private fun getAuthorities(accessLevel: Int?): Collection<GrantedAuthority> =
                CollectionUtils.collect<Role, SimpleGrantedAuthority>(
                        Role.getRolesByAccessLevel(accessLevel), { role -> SimpleGrantedAuthority(role.name) })
    }


}
