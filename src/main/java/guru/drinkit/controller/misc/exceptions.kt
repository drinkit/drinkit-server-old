package guru.drinkit.controller.misc

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author pkolmykov
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot process request body")
class BadRequestException : RuntimeException()

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User has not rights to perform this operation")
class ForbiddenActionException : RuntimeException()

