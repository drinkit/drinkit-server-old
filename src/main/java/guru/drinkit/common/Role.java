package guru.drinkit.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.collections4.Predicate;

import static org.apache.commons.collections4.CollectionUtils.filter;

public enum Role {

    ROLE_ADMIN(0), ROLE_USER(9);
    private final int accessLevel;

    Role(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public static List<Role> getRolesByAccessLevel(final Integer accessLevel) {
        List<Role> roles = new ArrayList<>(EnumSet.allOf(Role.class));
        filter(roles, new Predicate<Role>() {
            @Override
            public boolean evaluate(final Role role) {
                return role.accessLevel >= accessLevel;
            }
        });
        return roles;
    }

}
