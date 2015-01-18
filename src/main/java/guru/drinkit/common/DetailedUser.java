package guru.drinkit.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@SuppressWarnings("unused")
public class DetailedUser extends User {

    private final String displayName;

    public DetailedUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String displayName) {
        super(username, password, authorities);
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "DetailedUser{" +
                "displayName='" + displayName + '\'' +
                "} " + super.toString();
    }
}
