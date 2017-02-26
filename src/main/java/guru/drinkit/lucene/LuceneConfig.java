package guru.drinkit.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author pkolmykov
 */
@Configuration
public class LuceneConfig {

    public Analyzer analyzer() {
        return new RussianAnalyzer();
    }

    public IndexWriterConfig recipeIndexWriterConfig() {
        return new IndexWriterConfig(analyzer());
    }

}
