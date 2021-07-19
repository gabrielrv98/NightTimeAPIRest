package com.esei.grvidal.nightTimeApi.builders

import com.esei.grvidal.nightTimeApi.builders.MessageViewBuilder.Companion.createMessageView
import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.model.ReadState
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.ChatView

class ChatViewBuilder {

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
                messages = orderMessages(friendship.messages!!)
                        //if it is snap it will only show the last message
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
         *  Sorting algorithm for messages
         */
        private fun orderMessages(messages: Set<Message>):Set<Message>{
            val finalSet = mutableSetOf<Message>()

            //  First the messages are grouped by dates
            val map = messages.groupBy { message-> message.date }
                .toMutableMap()
            val orderedDays = map.keys.sorted()
            // After each day sorts its messages and its added to the final Set
            for(day in orderedDays){
                finalSet.addAll(map[day]!!.sortedBy { it.hour } )
            }

            return finalSet

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
                messages = messages.map { createMessageView(it) },
                unreadMessages = unreadMessages
            )
        }
    }
}