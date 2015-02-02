package guru.drinkit.service.impl;

import java.util.Collection;
import javax.annotation.Resource;

import guru.drinkit.common.DetailedUser;
import guru.drinkit.common.Role;
import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import org.apache.commons.collections4.Transformer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.apache.commons.collections4.CollectionUtils.collect;

@Component
public class BasicUserDetailsService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Test msg");
        }
        return new DetailedUser(user.getUsername(), user.getPassword(),
                getAuthorities(user.getAccessLevel()), user.getDisplayName());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Integer accessLevel) {
        return collect(Role.getRolesByAccessLevel(accessLevel), new Transformer<Role, SimpleGrantedAuthority>() {
            @Override
            public SimpleGrantedAuthority transform(final Role role) {
                return new SimpleGrantedAuthority(role.name());
            }
        });
    }

    public boolean createUser(User user) {
        boolean isNew = userRepository.findByUsername(user.getUsername()) == null;
        if(isNew){
            userRepository.save(user);
        }
        return isNew;
    }


}
