package guru.drinkit.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import guru.drinkit.common.Criteria;
import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.common.RecipeComparatorByCriteria;
import guru.drinkit.domain.Recipe;
import guru.drinkit.repository.RecipeRepository;
import guru.drinkit.service.FileStoreService;
import guru.drinkit.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Resource
    FileStoreService fileStoreService;

    @Resource
    private RecipeRepository recipeRepository;

    @Override
    public Recipe save(Recipe recipe) {
        boolean insert = recipe.getId() == null;
        if (insert) {
            Recipe lastRecipe = recipeRepository.findFirstByOrderByIdDesc();
            recipe.setId(lastRecipe == null ? 1 : lastRecipe.getId() + 1);
            recipe.setAddedBy(DrinkitUtils.getUserName());
            recipe.setCreatedDate(new Date());
            recipe.setStats(new Recipe.Stats(0, 0));
        }
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(int id) {
        recipeRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recipe> findByCriteria(Criteria criteria) {
        List<Recipe> recipes = recipeRepository.findByCriteria(criteria);
        Collections.sort(recipes, new RecipeComparatorByCriteria(criteria));
        return recipes;
    }

    @Override
    @Transactional(readOnly = true)
    public Recipe findById(int id) {
        return recipeRepository.findOne(id);
    }


    @Override
    public List<Recipe> findByRecipeNameContaining(String namePart) {
        return recipeRepository.findByNameContainingIgnoreCase(namePart);
    }


    @Override
    @Transactional
    public void saveMedia(int recipeId, byte[] image, byte[] thumbnail) throws IOException {
        Recipe recipe = recipeRepository.findOne(recipeId);
        recipe.setImageUrl(fileStoreService.getUrl(fileStoreService.save(recipeId, image, "image")));
        recipe.setThumbnailUrl(fileStoreService.getUrl(fileStoreService.save(recipeId, thumbnail, "thumbnail")));
        recipeRepository.save(recipe);
    }
}
