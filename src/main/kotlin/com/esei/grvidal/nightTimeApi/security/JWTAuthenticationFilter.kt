package com.esei.grvidal.nightTimeApi.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val EXPIRATION_TIME = 1209600000 // 2 weeks in ms
const val SECRET = "SomeSecretYouProbablyWantToMakeConfigurable"
const val AUTH_COOKIE = "AUTH_COOKIE"


class JWTAuthenticationFilter(private val authManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // When a POST request to /login is made, this method gets called. We are using a login form to authenticate
        // users, so lets grab the username and password that was provided in the form.
        val username = request.getHeader("username")
        val password = request.getHeader("password")

        logger.info("JWTAuthenticationFilter: username -> $username, password -> $password")

        // Get spring to authenticate the provided username and password.
        return authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse,
                                          chain: FilterChain, authResult: Authentication?) {

        val user = authResult?.principal as? User
                ?: throw IllegalArgumentException("authResult cannot be null and must be an instance of User")

        // Create the JWT token and store the JWT in a cookie.
        val token = JWT.create()
                .withSubject(user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET))
        response.addCookie(Cookie(AUTH_COOKIE, token))
        logger.info("JWTAuthenticationFilter: cookie -> $AUTH_COOKIE, token -> $token")
        chain.doFilter(request, response)
    }
}