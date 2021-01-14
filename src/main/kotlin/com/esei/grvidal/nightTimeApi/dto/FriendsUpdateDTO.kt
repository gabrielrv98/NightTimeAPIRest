package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions

data class FriendsUpdateDTO(
    val id: Long,
    val answer: AnswerOptions
)

