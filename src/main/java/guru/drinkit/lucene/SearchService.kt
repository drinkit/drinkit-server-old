package guru.drinkit.lucene

import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field.Store.NO
import org.apache.lucene.document.StoredField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

/**
 * @author pkolmykov
 */
@Service
open class SearchService @Autowired constructor(
        val recipeIndexWriter: IndexWriter,
        val analyzer: Analyzer,
        val recipeDirectory: Directory,
        val recipeRepository: RecipeRepository,
        val ingredientRepository: IngredientRepository
) {

    enum class RecipeFields {
        localizedName,
        originalName,
        description,
        ingredientName,
        ingredientDesc,
        ingredientAlias
    }

    @PostConstruct
    open fun index() {
        try {
            indexRecipes(recipeRepository.findAll())
        } catch (e: Exception) {
            e.printStackTrace()
            //todo rollbar
        }
    }

    private fun indexRecipes(recipes: Iterable<Recipe>) {
        val ingredients = ingredientRepository.findAll().associateBy { it.id!! }
        val documents = recipes.map { transformToDocument(it, ingredients) }
        recipeIndexWriter.addDocuments(documents)
        recipeIndexWriter.commit()
    }

    private fun transformToDocument(recipe: Recipe, ingredients: Map<Int, Ingredient>): Document {
        val document = Document()
        document.add(StoredField("id", recipe.id!!))
        document.add(TextField(RecipeFields.localizedName.name, recipe.name, NO).also { it.setBoost(2.9f) })
        recipe.originalName?.let { document.add(TextField(RecipeFields.originalName.name, it, NO).also { it.setBoost(2.1f) }) }
        document.add(TextField(RecipeFields.description.name, recipe.description, NO))
        recipe.ingredientsWithQuantities
                .map { it.ingredientId }
                .map { ingredients[it] }
                .forEach {
                    document.add(TextField(RecipeFields.ingredientName.name, it!!.name, NO).also { it.setBoost(1.5f) })
                    document.add(TextField(RecipeFields.ingredientDesc.name, it.description, NO).also { it.setBoost(0.5f) })
                    it.alias?.forEach { document.add(TextField(RecipeFields.ingredientAlias.name, it, NO).also { it.setBoost(0.5f) }) }
                }
        return document
    }

    fun findRecipes(searchString: String): List<Int> {
        val queryParser = MultiFieldQueryParser(RecipeFields.values().map { it.name }.toTypedArray(), analyzer)
        val searcher = IndexSearcher(DirectoryReader.open(recipeDirectory))
        val topDocs = searcher.search(queryParser.parse(searchString), 50)
        return topDocs.scoreDocs.map { searcher.doc(it.doc).getField("id").numericValue().toInt() }
    }

    fun indexRecipe(recipe: Recipe) {
        val ingredients = ingredientRepository.findAll().associateBy { it.id!! }
        recipeIndexWriter.addDocument(transformToDocument(recipe, ingredients))
    }
}
