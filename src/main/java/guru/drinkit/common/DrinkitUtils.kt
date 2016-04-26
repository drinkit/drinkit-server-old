package guru.drinkit.common

import guru.drinkit.domain.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.Assert

object DrinkitUtils {

    val LOGGER = LoggerFactory.getLogger("CommonLogging")

    fun logOperation(operation: String, obj: Any) {
        var info = String.format("%s: %s", operation, obj)
        if (SecurityContextHolder.getContext().authentication != null) {
            info += " by user: " + SecurityContextHolder.getContext().authentication.principal
        }
        LOGGER.info(info)
    }

    /**
     * Retrieve userId from security context

     * @return 'ANONYMOUS' for anonymous, 'username' if user authorized and is not admin, otherwise return null;
     */
    fun getUserNameAndId(): SimpleUser {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            if (authentication.authorities.size == 0 && authentication.principal is ExtendedUser) {
                val user = authentication.principal as ExtendedUser
                return SimpleUser(user.userId, user.username)
            }
        }
        return SimpleUser(null, User.ANONYMOUS_USER_NAME)
    }

    data class SimpleUser(val id: String?, val name: String)


    fun assertEqualsIds(id1: Int, id2: Int) {
        Assert.isTrue(id1 == id2, "id from uri and id from json should be identical")
    }

}
