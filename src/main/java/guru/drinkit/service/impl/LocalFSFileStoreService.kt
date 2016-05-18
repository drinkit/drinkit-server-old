package guru.drinkit.service.impl

import guru.drinkit.service.FileStoreService
import guru.drinkit.springconfig.WebConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 02.11.2014
 * Time: 14:01
 */
@Service
class LocalFSFileStoreService @Autowired constructor(
        val environment: Environment
) : FileStoreService {

    override fun save(recipeId: Int, image: ByteArray, mediaType: String): String {
        val mediaFolder = environment.getProperty("media.folder")
        val time = Date().time
        val filePrefix = "${recipeId}_${mediaType}_"
        val fileName = "$filePrefix$time.jpg"
        val filePath = mediaFolder + File.separator + fileName

        val folder = File(mediaFolder)
        folder.mkdirs()
        val files = folder.listFiles { dir, name -> name.startsWith(filePrefix) }
        if (files != null) {
            for (file in files) {
                file.delete()
            }
        }
        FileOutputStream(filePath).use { fileOutputStream -> fileOutputStream.write(image) }
        return fileName
    }

    override fun getUrl(fileName: String): String {
        return WebConfig.REST_ENDPOINT + WebConfig.MEDIA_ENDPOINT + "/" + fileName
    }
}
