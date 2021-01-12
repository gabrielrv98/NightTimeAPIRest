package com.esei.grvidal.nightTimeApi.security

import com.esei.grvidal.nightTimeApi.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(val user: User) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> { //NO admins yet
        return mutableListOf()
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.nickname
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
       return true
    }

}