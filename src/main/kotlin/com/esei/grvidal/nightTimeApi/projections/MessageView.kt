package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate
import java.time.LocalTime


/**
 * Interface to show a list of chats
 */
data class MessageView(
    var messageId: Long,
    var text: String,
    var date: LocalDate,
    var time: LocalTime,
    var user: Long
): java.io.Serializable