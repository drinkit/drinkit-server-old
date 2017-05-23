package guru.drinkit.springconfig

import org.apache.lucene.analysis.Analyzer
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

    //todo AnalWrapper
    @Bean open fun analyzer(): Analyzer = RussianAnalyzer()

    @Bean open fun recipeIndexWriter(directory: Directory, analyzer: Analyzer): IndexWriter
            = IndexWriter(directory, IndexWriterConfig(analyzer()))

}
