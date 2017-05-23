package guru.drinkit.lucene

import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
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

    @PostConstruct
    open fun index() {
        try {
            recipeIndexWriter.deleteAll()
            recipeIndexWriter.addDocuments(convertRecipes())
            recipeIndexWriter.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            //todo rollbar
        }
    }

    private fun convertRecipes(): List<Document> {
        val recipes = recipeRepository.findAll()
        val ingredients = ingredientRepository.findAll().associateBy { it.id!! }
        return recipes.map { transformToDocument(it, ingredients) }
    }

    private fun transformToDocument(recipe: Recipe, ingredients: Map<Int, Ingredient>): Document {
        val document = Document()
        document.add(StoredField("id", recipe.id!!))
        recipe.name?.let { document.add(RecipeFields.localizedName.toField(it)) }
        recipe.originalName?.let { document.add(RecipeFields.originalName.toField(it)) }
        recipe.description?.let { document.add(RecipeFields.description.toField(it)) }
        recipe.ingredientsWithQuantities
                .map { it.ingredientId }
                .mapNotNull { ingredients[it] }
                .forEach { ingredient ->
                    ingredient.name?.let { document.add(RecipeFields.ingredientName.toField(it)) }
                    ingredient.description?.let { document.add(RecipeFields.ingredientDesc.toField(it)) }
                    ingredient.alias?.forEach { document.add(RecipeFields.ingredientAlias.toField(it)) }
                }
        return document
    }

    fun findRecipes(searchString: String): Collection<Int> {
        val queryParser = MultiFieldQueryParser(RecipeFields.values().map { it.name }.toTypedArray(), analyzer)
        val searcher = IndexSearcher(DirectoryReader.open(recipeDirectory))
        val topDocs = searcher.search(queryParser.parse(searchString), 50)
        //todo fix duplicates
        return topDocs.scoreDocs.map { searcher.doc(it.doc).getField("id").numericValue().toInt() }
    }

    fun indexRecipe(recipe: Recipe) {
        val ingredients = ingredientRepository.findAll().associateBy { it.id!! }
        recipeIndexWriter.addDocument(transformToDocument(recipe, ingredients))
        recipeIndexWriter.commit()
    }

    companion object {
        enum class RecipeFields(val boost: Float) {
            localizedName(2.9f),
            originalName(2.1f),
            description(1.0f),
            ingredientName(1.5f),
            ingredientDesc(0.5f),
            ingredientAlias(0.5f);

            fun toField(value: String): Field {
                return TextField(this.name, value, NO).apply { setBoost(boost) }
            }
        }
    }
}
