package guru.drinkit.controller

import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.impl.BasicUserDetailsService
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
@RequestMapping("users")
open class UserController
@Autowired constructor(
        private val basicUserDetailsService: BasicUserDetailsService,
        private val userRepository: UserRepository) {

    @RequestMapping("/principal")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    fun principal(): Any = SecurityContextHolder.getContext().authentication.principal

    @RequestMapping("/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    fun currentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = (principal as UserDetails).username
        return userRepository.findOne(username)
    }


    @PreAuthorize("isAnonymous()")
    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/register", headers = arrayOf("Content-Type=application/x-www-form-urlencoded"))
    fun registerUser(@RequestParam email: String, @RequestParam password: String, @RequestParam displayName: String): ResponseEntity<User> {
        val user = User(username = email, password = password, displayName = displayName)
        val created = basicUserDetailsService.createUser(user)
        return ResponseEntity(if (created) HttpStatus.CREATED else HttpStatus.FORBIDDEN)
    }

}

