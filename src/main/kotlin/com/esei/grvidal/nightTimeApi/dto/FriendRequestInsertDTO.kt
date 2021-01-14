package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.User

data class FriendRequestInsertDTO(
        var idUserAsk: Long,
        val idUserAnswer: Long
)

fun FriendRequestInsertDTO.toFriendRequest(userAsk: User, userAnswer: User): Friends {
    return Friends(
            userAsk,
            userAnswer
    )
}
