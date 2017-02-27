package guru.drinkit.lucene

import guru.drinkit.domain.Recipe
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.document.*
import org.apache.lucene.document.Field.Store.NO
import org.apache.lucene.document.Field.Store.YES
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

/**
 * @author pkolmykov
 */
@Service
open class SearchService @Autowired constructor(
        val recipeIndexWriter: IndexWriter,
        val analyzer: Analyzer,
        val recipeDirectory: Directory
) {

    fun searchRecipes(searchString: String): List<Int> {
        val queryParser = MultiFieldQueryParser(arrayOf("id", "name", "originalName", "description"), analyzer)
        val searcher = IndexSearcher(DirectoryReader.open(recipeDirectory))
        val topDocs = searcher.search(queryParser.parse(searchString), 50)
        return topDocs.scoreDocs.map { searcher.doc(it.doc).getField("id").numericValue().toInt() }
    }

    fun indexRecipes(recipes: Iterable<Recipe>) {
        val documents = recipes.map { transformToDocument(it) }
        recipeIndexWriter.addDocuments(documents)
        recipeIndexWriter.commit()
    }

    private fun transformToDocument(recipe: Recipe): Document {
        val document = Document()
        document.add(StoredField("id", recipe.id!!))
        document.add(TextField("name", recipe.name, NO))
        document.add(TextField("originalName", recipe.originalName, NO))
        document.add(TextField("description", recipe.description, NO))
        return document
    }

    companion object {

        @Throws(IOException::class, ParseException::class)
        @JvmStatic fun main(args: Array<String>) {
            val analyzer = RussianAnalyzer()
            val index = RAMDirectory()
            val config = IndexWriterConfig(analyzer)

            val indexWriter = IndexWriter(index, config)
            val recipe = Recipe()
            recipe.id = 1
            recipe.name = "водка со льдом"
            recipe.description = "desc"
            val doc = Document()
            doc.add(IntField("id", recipe.id!!, YES))
            doc.add(TextField("name", recipe.name!!, Field.Store.NO))
            doc.add(TextField("desc", recipe.description!!, Field.Store.NO))
            indexWriter.addDocument(doc)
            indexWriter.commit()
            //        recipeIndexWriter.close();
            //        recipeIndexWriter.close();
            val doc2 = Document()
            doc2.add(IntField("id", recipe.id!!, YES))
            doc2.add(TextField("name", "вискарь", Field.Store.NO))
            doc2.add(TextField("desc", "вкусный вискарь", Field.Store.NO))
            indexWriter.addDocument(doc2)
            indexWriter.commit()
            val query = MultiFieldQueryParser(arrayOf("name", "desc"), analyzer).parse("вискарь водка")
            println(query)
            val searcher = IndexSearcher(DirectoryReader.open(index))
            val res = searcher.search(query, 10)
            println(Arrays.toString(res.scoreDocs))
            println(searcher.doc(res.scoreDocs[0].doc))
        }
    }
}
