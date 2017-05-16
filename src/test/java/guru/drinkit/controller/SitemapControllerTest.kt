package guru.drinkit.controller

import guru.drinkit.controller.common.AbstractMockMvcTest
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */
class SitemapControllerTest : AbstractMockMvcTest() {
    @Test
    fun getSitemap() {
        mockMvc.perform(get("/sitemap/"))
                .andExpect(status().isOk)
    }

}