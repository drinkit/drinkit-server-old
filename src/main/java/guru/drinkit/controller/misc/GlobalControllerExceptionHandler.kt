package guru.drinkit.controller.misc

import com.rollbar.Rollbar
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest

/**
 * @author pkolmykov
 */
@ControllerAdvice
class GlobalControllerExceptionHandler
@Autowired
constructor(@Value("\${rollbar.token}") token: String, @Value("\${rollbar.env}") env: String) {
    private val rollbar: Rollbar = Rollbar(token, env)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(request: HttpServletRequest, e: RuntimeException) {
        if (e is AccessDeniedException || AnnotationUtils.findAnnotation(e.javaClass, ResponseStatus::class.java) != null) {
            throw e
        }
        getLogger(GlobalControllerExceptionHandler::class.java).error("Global exception", e)
        logToRollbar(e, request)
    }

    private fun logToRollbar(e: RuntimeException, request: HttpServletRequest) {
        rollbar.log(e, mapOf(
                Pair("user", "${request.remoteUser} ${(request.userPrincipal as UsernamePasswordAuthenticationToken).authorities}"),
                Pair("body", IOUtils.toString(request.inputStream, Charset.defaultCharset()))

        ), "${request.method} ${request.requestURI}")
    }

}
