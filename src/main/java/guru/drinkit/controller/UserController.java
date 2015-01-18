package guru.drinkit.controller;

import guru.drinkit.domain.User;
import guru.drinkit.service.impl.BasicUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    BasicUserDetailsService basicUserDetailsService;

    @RequestMapping("/getInfo")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Object getCurrentUserInfo() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
