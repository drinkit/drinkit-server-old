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

    private lateinit var recipeSearcher: IndexSearcher

    @PostConstruct
    open fun index() {
        try {
            recipeIndexWriter.deleteAll()
            recipeIndexWriter.addDocuments(convertRecipes())
            recipeIndexWriter.commit()
            recipeSearcher = IndexSearcher(DirectoryReader.open(recipeDirectory))
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
        recipe.ingredientsWithQuantities.asSequence()
                .map { it.ingredientId }
                .mapNotNull { ingredients[it] }
                .forEach { ingredient ->
                    ingredient.name?.let { document.add(RecipeFields.ingredientName.toField(it)) }
                    ingredient.alias?.forEach { document.add(RecipeFields.ingredientAlias.toField(it)) }
                }
        return document
    }

    private val recipeQueryParser = recipeQueryParser()

    fun findRecipes(searchString: String): Collection<Int> {
        val topDocs = recipeSearcher.search(recipeQueryParser.parse(searchString), 50)
        return topDocs.scoreDocs.map { recipeSearcher.doc(it.doc).getField("id").numericValue().toInt() }
    }

    fun indexRecipe(recipe: Recipe) {
        val ingredients = ingredientRepository.findAll().associateBy { it.id!! }
        recipeIndexWriter.addDocument(transformToDocument(recipe, ingredients))
        recipeIndexWriter.commit()
    }

    private fun recipeQueryParser(): MultiFieldQueryParser = MultiFieldQueryParser(
            RecipeFields.values().map { it.name }.toTypedArray(),
            analyzer,
            RecipeFields.values().associate { it.name to it.boost }
    )

    companion object {
        enum class RecipeFields(val boost: Float) {
            localizedName(2.9f),
            originalName(2.1f),
            description(0.1f),
            ingredientName(1.5f),
            ingredientAlias(1f);

            fun toField(value: String): Field {
                return TextField(this.name, value, NO)
            }
        }
    }
}
