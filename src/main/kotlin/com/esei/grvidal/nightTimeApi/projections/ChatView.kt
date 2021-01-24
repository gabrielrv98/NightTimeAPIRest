package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.model.User


/**
 * Interface to show a list of chats
 */
class ChatView(
    var friendshipId: Long,
    var userId: Long,
    var userNickname: String,
    var messages: List<MessageView>
) : java.io.Serializable {

    /**
     *  Main constructor that receives a Friendship and an ID from the user who made the request,
     *  extracts the other user and calls the private constructor
     */
    constructor(
        friendship: Friendship,
        userId: Long
    ) : this(
        friendshipId = friendship.id,
        user = if (friendship.userAsk.id == userId) friendship.userAnswer
        else friendship.userAsk,
        messages = friendship.messages!!
    )

    /**
     *  Private constructor, receives the friendship ID, the set of messages an the oth
     *
     */
    private constructor(
        friendshipId: Long,
        user: User,
        messages: Set<Message>
    ) : this(
        friendshipId = friendshipId,
        userId = user.id,
        userNickname = user.nickname,
        messages = messages.map { MessageView(it) }
    )


}