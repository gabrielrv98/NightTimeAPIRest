package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.utils.AnswerOptions


data class UserProfileView(
    val id: Long,
    val name: String,
    val nickname: String,
    val state: String,
    var nextDate: DateCityProjection?,
    val picture: String?,
    val friendshipState: AnswerOptions
) {

    constructor(user: UserProjection, friendshipState: AnswerOptions): this(
        id = user.getId(),
        name = user.getName(),
        nickname = user.getNickname(),
        state = user.getState(),
        nextDate = user.getNextDate(),
        picture = user.getPicture(),
        friendshipState = friendshipState
    )
}