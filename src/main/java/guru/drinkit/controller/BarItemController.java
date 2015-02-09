package guru.drinkit.controller;

import java.util.List;

import guru.drinkit.domain.BarItem;
import guru.drinkit.service.BarItemsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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

    @RequestMapping(value = "{ingredientId}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable ObjectId itemId, @PathVariable Integer ingredientId){
        barItemsService.delete(itemId, ingredientId);
    }

    @RequestMapping(value = "{ingredientId}", method = RequestMethod.PATCH)
    public void changeStatus(@PathVariable ObjectId userId, @PathVariable Integer ingredientId, boolean isActive){
        barItemsService.changeStatus(userId, ingredientId, isActive);
    }

    @RequestMapping(value = "{ingredientId}", method = RequestMethod.PUT)
    public BarItem edit(@PathVariable ObjectId userId, @PathVariable Integer ingredientId, BarItem barItem){
        Assert.isTrue(barItem.getUserId().equals(userId) && barItem.getIngredientId().equals(ingredientId));
        return barItemsService.edit(barItem);
    }

}
