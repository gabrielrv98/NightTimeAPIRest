package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.User
import java.time.LocalDate

class UserDTOEdit(
        val name: String?,
        var password: String?,
        val state: String? = null,
        val email: String?
)

