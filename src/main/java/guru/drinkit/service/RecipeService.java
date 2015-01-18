package guru.drinkit.service;

import guru.drinkit.common.Criteria;
import guru.drinkit.domain.Recipe;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.List;

public interface RecipeService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Recipe save(Recipe recipe);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(int id);

    List<Recipe> findAll();

    List<Recipe> findByCriteria(Criteria criteria);

    List<Recipe> findByRecipeNameContaining(String namePart);

    Recipe findById(int id);

    void saveMedia(int recipeId, byte[] image, byte[] thumbnail) throws IOException;
}
