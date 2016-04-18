package guru.drinkit.repository

import guru.drinkit.AbstractBaseTest
import guru.drinkit.domain.Comment
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * @author pkolmykov
 */
class CrudRepositoriesIT : AbstractBaseTest() {

    @Autowired lateinit var commentRepository: CommentRepository

    @Test
    fun testComment() {
        val raw = Comment(null, 1, Date(), Comment.Author(ObjectId().toHexString(), "4yvak"), "bla bla bla")
        val id = commentRepository.insert(raw).id
        assertThat(id).isNotNull();
        val inserted = commentRepository.findOne(id)
        assertThat(raw).isEqualTo(inserted)
    }
}