package guru.drinkit.controller

import guru.drinkit.controller.UserController.Companion.RESOURCE_NAME
import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.BasicUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(RESOURCE_NAME)
open class UserController @Autowired constructor(
        private val basicUserDetailsService: BasicUserDetailsService,
        private val userRepository: UserRepository) {

    companion object {
        const val RESOURCE_NAME = "users";
    }

    @RequestMapping("/principal")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    open fun principal(): Any = SecurityContextHolder.getContext().authentication.principal

    @RequestMapping("/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    open fun currentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = (principal as UserDetails).username
        return userRepository.findByUsername(username)!!
    }


    @PreAuthorize("permitAll")
    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/register", headers = arrayOf("Content-Type=application/x-www-form-urlencoded"))
    open fun registerUser(@RequestParam email: String, @RequestParam password: String, @RequestParam displayName: String): ResponseEntity<User> {
        val user = User(username = email, password = password, displayName = displayName)
        val created = basicUserDetailsService.createUser(user)
        return ResponseEntity(if (created) HttpStatus.CREATED else HttpStatus.FORBIDDEN)
    }

}

