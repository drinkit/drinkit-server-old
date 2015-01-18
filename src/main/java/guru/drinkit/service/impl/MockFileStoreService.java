package guru.drinkit.service.impl;

import guru.drinkit.service.FileStoreService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 02.11.2014
 * Time: 14:07
 */
@Service
@Primary
@Profile("test")
public class MockFileStoreService implements FileStoreService {


    private String mockFileName;

    public MockFileStoreService() {
        mockFileName = "defaultFileName.ext";
    }

    @Override
    public String save(int recipeId, byte[] thumbnail, String thumbnail1) throws IOException {
        return mockFileName;
    }

    @Override
    public String getUrl(String fileName) {
        return fileName;
    }
}
