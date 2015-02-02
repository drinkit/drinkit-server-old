package guru.drinkit.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;

import guru.drinkit.service.FileStoreService;
import guru.drinkit.springconfig.WebConfig;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
        final String filePrefix = recipeId + "_" + mediaType + "_";
        String fileName = filePrefix + time + ".jpg";
        String filePath = mediaFolder + File.separator + fileName;

        File folder = new File(mediaFolder);
        folder.mkdirs();
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith(filePrefix);
            }
        });
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
