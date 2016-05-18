package guru.drinkit.common;

import org.junit.Assert;
import org.junit.Test;

public class RoleTest {

    @Test
    public void testGetRolesByAccessLevel() throws Exception {
        Assert.assertTrue(Role.getRolesByAccessLevel(0).contains(Role.ROLE_ADMIN));
        Assert.assertTrue(Role.getRolesByAccessLevel(0).contains(Role.ROLE_USER));
        Assert.assertFalse(Role.getRolesByAccessLevel(9).contains(Role.ROLE_ADMIN));
    }
}