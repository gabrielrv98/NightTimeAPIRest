package com.esei.grvidal.nightTimeApi.security


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.util.WebUtils
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter @Autowired constructor(authManager: AuthenticationManager)
    : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        // Get the authentication cookie. This cookie holds the JWT.
        val cookie = WebUtils.getCookie(request, AUTH_COOKIE)

        if (cookie == null || cookie.value == null || cookie.value.trim().isEmpty()) {
            // If there is no cookie, the user is not authenticated. Continue the filter chain.
            chain.doFilter(request, response)
            return
        }

        // There is an authentication cookie. Verify the JWT and and set the authentication token on
        // springs security context. This lets spring know who the logged in user is.
        val authenticationToken = getAuthenticationToken(cookie.value)
        SecurityContextHolder.getContext().authentication = authenticationToken
        chain.doFilter(request, response)
    }

    private fun getAuthenticationToken(token: String): UsernamePasswordAuthenticationToken? {

        // Parse and verify the provided token.
        val parsedToken = JWT.require(Algorithm.HMAC512(SECRET.toByteArray()))
                .build()
                .verify(token)
                .subject ?: return null

        return UsernamePasswordAuthenticationToken(parsedToken, null, listOf())
    }
}