package guru.drinkit.lucene;

import guru.drinkit.domain.Recipe;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author pkolmykov
 */
@Service
public class SearchService {

    public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer = new RussianAnalyzer();
        final RAMDirectory index = new RAMDirectory();
        final IndexWriterConfig config = new IndexWriterConfig(analyzer);

        final IndexWriter indexWriter = new IndexWriter(index, config);
        final Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("водка со льдом");
        recipe.setDescription("desc");
        final Document doc = new Document();
        doc.add(new IntField("id", recipe.getId(), Field.Store.YES));
        doc.add(new TextField("name", recipe.getName(), Field.Store.NO));
        doc.add(new TextField("desc", recipe.getDescription(), Field.Store.NO));
        indexWriter.addDocument(doc);
        indexWriter.commit();
//        indexWriter.close();
//        indexWriter.close();
        final Document doc2 = new Document();
        doc2.add(new IntField("id", recipe.getId(), Field.Store.YES));
        doc2.add(new TextField("name", "вискарь", Field.Store.NO));
        doc2.add(new TextField("desc", "вкусный вискарь", Field.Store.NO));
        indexWriter.addDocument(doc2);
        indexWriter.commit();
        final Query query = new MultiFieldQueryParser(new String[]{"name", "desc"}, analyzer).parse("вискарь водка");
        System.out.println(query);
        final IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(index));
        final TopDocs res = searcher.search(query, 10);
        System.out.println(Arrays.toString(res.scoreDocs));
        System.out.println(searcher.doc(res.scoreDocs[0].doc));
    }

    public void indexRecipes(Iterable<Recipe> recipes) {
//        recipes.
//        transformToDocument();
    }
}
