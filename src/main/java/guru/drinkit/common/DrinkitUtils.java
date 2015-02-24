package guru.drinkit.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class DrinkitUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger("CommonLogging");

    public static void logOperation(String operation, Object obj) {
        String info = String.format("%s: %s", operation, obj);
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            info += " by user: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        LOGGER.info(info);
    }

    /**
     * Retrieve userId from security context
     *
     * @return 'ANONYMOUS' for anonymous, 'username' if user authorized and is not admin, otherwise return null;
     */
    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getAuthorities().size() != 0 &&
                    authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
                return "ANONYMOUS";
            } else if (!((UserDetails) authentication.getPrincipal()).getAuthorities()
                    .contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))) {
                return ((DetailedUser) authentication.getPrincipal()).getUsername();
            }
        }
        return null;
    }

    public static void assertEqualsIds(final int id1, final int id2) {
        Assert.isTrue(id1 == id2, "id from uri and id from json should be identical");
    }

}
