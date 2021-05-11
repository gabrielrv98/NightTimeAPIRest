package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByFriendship_Id(chat_id: Long) : List<Message>

    @Transactional
    @Modifying
    @Query(value = "UPDATE Message AS M SET M.readState = com.esei.grvidal.nightTimeApi.model.ReadState.READ " +
            "WHERE M.friendship.id = ?1 and M.user.id <> ?2 ")
    fun markAllAsReadByFriendshipId(idFriendship: Long, idUser: Long)

}