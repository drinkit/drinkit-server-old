package guru.drinkit.controller;

import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.impl.BasicUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private BasicUserDetailsService basicUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/principal")
     @ResponseBody
     @PreAuthorize("isAuthenticated()")
     public Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @RequestMapping("/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }



    @PreAuthorize("isAnonymous()")
    @RequestMapping(method = RequestMethod.POST, value = "/register", headers = "Content-Type=application/x-www-form-urlencoded")
    public ResponseEntity<User> registerUser(@RequestParam String email, @RequestParam String password, @RequestParam String displayName) {
        User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setDisplayName(displayName);
        user.setAccessLevel(User.ACCESS_LVL_USER);
        boolean created = basicUserDetailsService.createUser(user);
        return new ResponseEntity<>(created ? HttpStatus.CREATED : HttpStatus.FORBIDDEN);
    }

}
