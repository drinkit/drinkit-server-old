package guru.drinkit.controller;

import java.util.List;

import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pkolmykov
 */
@Controller
@RequestMapping("users/{userId}/barItems")
public class UserBarController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User.BarItem> getBarItems(@PathVariable String userId) {
        return userRepository.findOne(userId).getBarItems();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNew(@PathVariable String userId, @RequestBody User.BarItem barItem) {
        userRepository.addBarItem(userId, barItem);
    }

    @RequestMapping(value = "{ingredientId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String userId, @PathVariable Integer ingredientId, @RequestBody User.BarItem barItem) {
        userRepository.updateBarItem(userId, barItem);
    }

    @RequestMapping(value = "{ingredientId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String userId, @PathVariable Integer ingredientId) {
        userRepository.removeBarItem(userId, ingredientId);
    }

}
