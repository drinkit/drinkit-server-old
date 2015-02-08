package guru.drinkit.service;

import java.util.List;

import guru.drinkit.domain.BarItem;
import guru.drinkit.repository.BarItemsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pkolmykov on 2/8/2015.
 */
@Service
public class BarItemsService {
    @Autowired
    BarItemsRepository barItemsRepository;


    public BarItem create(final BarItem barItem) {
        return barItemsRepository.save(barItem);
    }

    public void delete(final ObjectId itemId) {
        barItemsRepository.delete(itemId);
    }

    public void changeStatus(final ObjectId itemId, final boolean isActive) {
        barItemsRepository.
    }

    public List<BarItem> findAll(final ObjectId userId) {
        return barItemsRepository.find;
    }
}
