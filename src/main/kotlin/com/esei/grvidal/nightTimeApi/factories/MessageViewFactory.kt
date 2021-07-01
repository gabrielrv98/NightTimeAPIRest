package com.esei.grvidal.nightTimeApi.factories

import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.MessageView

class MessageViewFactory {

    companion object {
        /**
         *  Main constructor that receives a Friendship and an ID from the user who made the request,
         *  extracts the other user and calls the private constructor
         */
        fun createMessageView(
            message: Message
        ): MessageView {
            return MessageView(
                messageId = message.id,
                text = message.text,
                date = message.date,
                time = message.hour,
                user = message.user.id
            )
        }
    }
}