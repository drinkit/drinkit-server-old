package guru.drinkit.service;

import guru.drinkit.domain.Ingredient;
import guru.drinkit.exception.RecipesFoundException;

import java.util.List;

public interface IngredientService {

    List<Ingredient> getIngredients();

    Ingredient getIngredientById(int id);

    Ingredient save(Ingredient ingredient);

    void delete(int id) throws RecipesFoundException;
}
