package guru.drinkit.service.impl;

import guru.drinkit.common.Criteria;
import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.common.RecipeComparatorByCriteria;
import guru.drinkit.domain.Recipe;
import guru.drinkit.repository.RecipeRepository;
import guru.drinkit.service.FileStoreService;
import guru.drinkit.service.RecipeService;
import guru.drinkit.service.aspect.EnableStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Resource
    FileStoreService fileStoreService;

    @Resource
    private RecipeRepository recipeRepository;

    @Override
    public Recipe save(Recipe recipe) {
        if (recipe.getId() == null) {
            Recipe lastRecipe = recipeRepository.findFirstByOrderByIdDesc();
            recipe.setId(lastRecipe == null ? 1 : lastRecipe.getId() + 1);
            recipe.setAddedBy(DrinkitUtils.getUserName());
            recipe.setCreatedDate(new Date());
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
    @EnableStats
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
