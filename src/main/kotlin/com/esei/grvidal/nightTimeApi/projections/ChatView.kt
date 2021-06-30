package com.esei.grvidal.nightTimeApi.projections

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
) : java.io.Serializable
