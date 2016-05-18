package guru.drinkit.service.impl

import guru.drinkit.service.FileStoreService
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 02.11.2014
 * Time: 14:07
 */
@Service
@Primary
@Profile("test")
class MockFileStoreService : FileStoreService {


    private val mockFileName: String

    init {
        mockFileName = "defaultFileName.ext"
    }

    override fun save(recipeId: Int, image: ByteArray, mediaType: String): String {
        return mockFileName
    }

    override fun getUrl(fileName: String): String {
        return fileName
    }
}
