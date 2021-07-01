package com.esei.grvidal.nightTimeApi.factories

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.model.ReadState
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.ChatView

class ChatViewFactory {

    companion object {
        /**
         *  Main constructor that receives a Friendship and an ID from the user who made the request,
         *  extracts the other user and calls the private constructor
         */
        fun createChatView(
            friendship: Friendship,
            userId: Long,
            isSnap: Boolean
        ): ChatView {
            return constructChatView(
                friendshipId = friendship.id,
                user = if (friendship.userAsk.id == userId) friendship.userAnswer
                else friendship.userAsk,
                messages = friendship.messages!!
                    .sortedBy { message ->
                        message.hour
                        message.date
                    }//sets on index 0 the most recent
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
        }


        /**
         *  Private constructor, receives the friendship ID, the set of messages an the oth
         *
         */
        private fun constructChatView(
            friendshipId: Long,
            user: User,
            messages: Set<Message>,
            unreadMessages: Int
        ): ChatView {
            return ChatView(
                friendshipId = friendshipId,
                userId = user.id,
                userNickname = user.nickname,
                hasImage = user.picture != null,
                messages = messages.map { MessageViewFactory.createMessageView(it) },
                unreadMessages = unreadMessages
            )
        }
    }
}