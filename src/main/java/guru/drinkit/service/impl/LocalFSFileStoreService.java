package guru.drinkit.service.impl;

import guru.drinkit.service.FileStoreService;
import guru.drinkit.springconfig.WebConfig;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 02.11.2014
 * Time: 14:01
 */
@Service
public class LocalFSFileStoreService implements FileStoreService {
    @Resource
    private Environment environment;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public String save(int recipeId, byte[] media, String mediaType) throws IOException {
        String mediaFolder = environment.getProperty("media.folder");
        long time = new Date().getTime();
        String filePrefix = recipeId + "_" + mediaType + "_";
        String fileName = filePrefix + time + ".jpg";
        String filePath = mediaFolder + File.separator + fileName;

        File folder = new File(mediaFolder);
        folder.mkdirs();
        File[] files = folder.listFiles((dir, name) -> name.startsWith(filePrefix));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(media);
        }
        return fileName;
    }

    @Override
    public String getUrl(String fileName) {
        return WebConfig.REST_ENDPOINT + WebConfig.MEDIA_ENDPOINT + "/" + fileName;
    }
}
