package guru.drinkit.common

import guru.drinkit.service.BasicUserDetailsService
import guru.drinkit.service.getRolesByAccessLevel
import org.junit.Assert
import org.junit.Test

class RoleTest {

    @Test
    @Throws(Exception::class)
    fun testGetRolesByAccessLevel() {
        Assert.assertTrue(getRolesByAccessLevel(0).contains(BasicUserDetailsService.Role.ROLE_ADMIN))
        Assert.assertTrue(getRolesByAccessLevel(0).contains(BasicUserDetailsService.Role.ROLE_USER))
        Assert.assertFalse(getRolesByAccessLevel(9).contains(BasicUserDetailsService.Role.ROLE_ADMIN))
    }
}