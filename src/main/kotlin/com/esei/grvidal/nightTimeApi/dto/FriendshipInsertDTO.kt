package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.User

data class FriendshipInsertDTO(
        var idUserAsk: Long,
        val idUserAnswer: Long
)

fun FriendshipInsertDTO.toFriendRequest(userAsk: User, userAnswer: User): Friendship {
    return Friendship(
            userAsk,
            userAnswer
    )
}
