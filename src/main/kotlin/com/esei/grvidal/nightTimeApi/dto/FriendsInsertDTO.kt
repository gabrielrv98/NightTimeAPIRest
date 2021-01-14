package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.User

data class FriendsInsertDTO(
        var idUserAsk: Long,
        val idUserAnswer: Long
)

fun FriendsInsertDTO.toFriendRequest(userAsk: User, userAnswer: User): Friends {
    return Friends(
            userAsk,
            userAnswer
    )
}
