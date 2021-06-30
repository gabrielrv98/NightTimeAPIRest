package com.esei.grvidal.nightTimeApi.dto

data class UserDTOInsert(
        val name: String,
        val nickname: String,
        var password: String,
        val state: String? = null,
        val email: String
)