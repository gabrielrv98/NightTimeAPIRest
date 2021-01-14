package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface FriendsRepository : JpaRepository<Friends, Long> {

    //Returns all the FriendsShips where an user is involved
    fun findFriendsByUserAsk_IdAndUserAnswer_Id(user1_id: Long, user2_id: Long) : List<Friends>

    //Returns all the FriendsShips where an user is involved and has a specific answer
    fun findFriendsByUserAsk_IdOrUserAnswer_IdAndAnswer(user1_id: Long, user2_id: Long, answer: AnswerOptions) : List<FriendProjection>

    //Returns a friendShip with the specified users IDs
    fun findUserAsk_IdAndUserAnswer_Id(user1_id: Long, user2_id: Long) : Optional<Friends>

    fun findFriendsByMessagesIsNotNullAndUser1_IdOrUser2_IdAndAnswer(user1_id: Long, user2_id: Long, answer: AnswerOptions): List<Friends>//todo List<FriendsChatProjection>
    fun existsFriendsByUser1_IdOrUser2_Id(user1_id: Long, user2_id: Long) : Boolean

}