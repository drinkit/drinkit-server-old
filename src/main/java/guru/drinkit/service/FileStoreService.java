package guru.drinkit.service;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 02.11.2014
 * Time: 14:00
 */
public interface FileStoreService {
    String save(int recipeId, byte[] image, String mediaType) throws IOException;

    String getUrl(String fileName);
}
