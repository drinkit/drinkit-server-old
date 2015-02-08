package guru.drinkit.controller;

import java.util.List;

import guru.drinkit.domain.BarItem;
import guru.drinkit.service.BarItemsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pkolmykov on 2/8/2015.
 */
@Controller
@RequestMapping("user/{userId}/barItems")
public class BarItemController {

    @Autowired
    BarItemsService barItemsService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BarItem> findAll(@PathVariable ObjectId userId){
        return barItemsService.findAll(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public BarItem newItem(BarItem barItem){
        return barItemsService.create(barItem);
    }

    @RequestMapping(value = "${itemId}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable ObjectId itemId){
        barItemsService.delete(itemId);
    }

    @RequestMapping(value = "${itemId}", method = RequestMethod.PUT)
    public void changeStatus(@PathVariable ObjectId itemId, boolean isActive){
        barItemsService.changeStatus(itemId, isActive);
    }

}
