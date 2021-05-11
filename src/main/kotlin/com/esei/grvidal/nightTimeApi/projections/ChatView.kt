package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.model.ReadState
import com.esei.grvidal.nightTimeApi.model.User


/**
 * Interface to show a list of chats
 */
class ChatView(
    var friendshipId: Long,
    var userId: Long,
    var userNickname: String,
    val hasImage: Boolean,
    var messages: List<MessageView>,
    val unreadMessages: Int
) : java.io.Serializable {

    /**
     *  Main constructor that receives a Friendship and an ID from the user who made the request,
     *  extracts the other user and calls the private constructor
     */
    constructor(
        friendship: Friendship,
        userId: Long,
        isSnap: Boolean
    ) : this(
        friendshipId = friendship.id,
        user = if (friendship.userAsk.id == userId) friendship.userAnswer
        else friendship.userAsk,
        messages = friendship.messages!!
            .sortedBy { message -> message.date }//sets on index 0 the most recent
            .sortedBy { message -> message.hour }
            .toMutableSet().apply {
                if (isSnap) {
                    this.removeAll(
                        this.subtract(listOf(this.last()))
                    )
                }
            },
        unreadMessages = friendship.messages!!
            .filter { it.user.id != userId } // Messages sent by a different user than userId
            .count { it.readState == ReadState.NOT_READ } // Messages that are not read

    )

    /**
     *  Private constructor, receives the friendship ID, the set of messages an the oth
     *
     */
    private constructor(
        friendshipId: Long,
        user: User,
        messages: Set<Message>,
        unreadMessages: Int
    ) : this(
        friendshipId = friendshipId,
        userId = user.id,
        userNickname = user.nickname,
        hasImage = user.picture != null,
        messages = messages.map { MessageView(it) },
        unreadMessages = unreadMessages
    )


}
