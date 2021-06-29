package com.esei.grvidal.nightTimeApi.init

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

data class MessageInit(
    val txt: String,
    val user: Int
)

class ChatInit(
    private val friendship: Friendship?,
    private val messageRepositories: MessageRepository,
    private val msgList: List<MessageInit>
) {

    fun saveData() {
        var i: Long = 0
        var j: Long = 0

        friendship?.let{ friendship ->
            for (msg in this.msgList) {

                i += if (Random(LocalTime.now().nano.toLong()).nextInt() % 2 == 0) {
                    0
                } else 1

                messageRepositories.save(
                    Message(
                        msg.txt,
                        LocalDate.now(),
                        LocalTime.now().plusMinutes(i).plusSeconds(j),
                        friendship,
                        user = if (msg.user == 0) friendship.userAsk
                        else friendship.userAnswer
                    )
                )
                j++
            }
        }

    }

}