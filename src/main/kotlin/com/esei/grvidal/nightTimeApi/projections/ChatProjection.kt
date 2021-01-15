package com.esei.grvidal.nightTimeApi.projections


/**
 * Interface to show a list of chats
 */
interface ChatProjection {

    fun getId(): Long
    fun getUserAsk(): UserChat
    fun getUserAnswer(): UserChat
    fun getMessages(): Set<MessageProjection>?


}

interface UserChat {
    fun getId(): Long
    fun getNickname(): String
}
