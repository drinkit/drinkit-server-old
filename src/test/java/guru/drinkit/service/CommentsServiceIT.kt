package guru.drinkit.service

import guru.drinkit.AbstractBaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author pkolmykov
 */

class CommentsServiceIT : AbstractBaseTest() {

    @Autowired lateinit var commentService: CommentService

    @Test
    fun crudTest() {
        //        commentService.insert(Comment(text = ))

        throw UnsupportedOperationException()
    }
}