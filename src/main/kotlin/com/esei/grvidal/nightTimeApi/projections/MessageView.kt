package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.Message
import java.time.LocalDate
import java.time.LocalTime


/**
 * Interface to show a list of chats
 */
class MessageView(
    var messageId: Long,
    var text: String,
    var date: LocalDate,
    var hour: LocalTime,
    var user: Long
): java.io.Serializable {


    /**
     *  Main constructor that receives a Friendship and an ID from the user who made the request,
     *  extracts the other user and calls the private constructor
     */
    constructor(
        message: Message
    ) : this(
        messageId= message.id,
        text = message.text,
        date = message.date,
        hour = message.hour,
        user = message.user.id
    )


}
