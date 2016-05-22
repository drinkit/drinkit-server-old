package guru.drinkit

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

/**
 * @author pkolmykov
 */
fun MockHttpServletRequestBuilder.copy(): MockHttpServletRequestBuilder {
    return this;
}