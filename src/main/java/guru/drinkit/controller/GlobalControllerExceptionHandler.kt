package guru.drinkit.controller

import com.rollbar.Rollbar
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
import java.util.stream.Collectors.joining
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
                Pair("body", request.inputStream.bufferedReader().lines().collect(joining()))

        ), "${request.method} ${request.requestURI}")
    }

}
