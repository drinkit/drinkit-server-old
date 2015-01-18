package guru.drinkit.service.impl;

import guru.drinkit.domain.Ingredient;
import guru.drinkit.exception.RecipesFoundException;
import guru.drinkit.exception.RecordNotFoundException;
import guru.drinkit.repository.IngredientRepository;
import guru.drinkit.repository.RecipeRepository;
import guru.drinkit.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public List<Ingredient> getIngredients() {
//        LOGGER.info("getIngredients: found {} records", ingredients.size());
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient getIngredientById(int id) {
        return ingredientRepository.findOne(id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        if (ingredient.getId() == null) {
            Ingredient lastIngredient = ingredientRepository.findFirstByOrderByIdDesc();
            ingredient.setId(lastIngredient == null ? 1 : lastIngredient.getId() + 1);
        }
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void delete(int id) throws RecipesFoundException {
        Ingredient ingredient = ingredientRepository.findOne(id);
        if (ingredient == null) {
            throw new RecordNotFoundException("Ingredient not found : " + id);
        }

        Integer count = recipeRepository.countByIngredientId(id);
        if (count == 0) {
            ingredientRepository.delete(id);
        } else {
            throw new RecipesFoundException(count);
        }
        ingredientRepository.delete(id);
    }
}
