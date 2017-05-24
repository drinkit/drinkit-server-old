package guru.drinkit.springconfig

import guru.drinkit.lucene.SearchService
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper
import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * @author pkolmykov
 */
@Configuration
@ComponentScan("guru.drinkit.lucene")
open class LuceneConfig {

    @Bean open fun recipeDirectory() = RAMDirectory()

    @Bean open fun analyzer(): Analyzer = PerFieldAnalyzerWrapper(
            RussianAnalyzer(),
            mapOf(
                    SearchService.Companion.RecipeFields.originalName.name to EnglishAnalyzer(),
                    SearchService.Companion.RecipeFields.description.name to descriptionAnalyzer()
            ))

    @Bean open fun recipeIndexWriter(directory: Directory, analyzer: Analyzer): IndexWriter
            = IndexWriter(directory, IndexWriterConfig(analyzer()))

    @Bean open fun descriptionAnalyzer(): Analyzer = RussianAnalyzer(
            RussianAnalyzer.getDefaultStopSet().apply {
                add("рецепт")
                add("коктейль")
            })

}
