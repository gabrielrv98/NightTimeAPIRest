package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {

    //Returns a projection of a Friendship
    fun findFriendshipById(id: Long): Optional<FriendshipProjection>

    //Returns all the FriendsShips where an user is involved
    //fun findFriendsByUserAsk_IdOrUserAnswer_Id(user1_id: Long, user2_id: Long) : List<Friendship>

    //Returns all the FriendsShips where an user is involved and has a specific answer
    fun findFriendshipByUserAsk_IdOrUserAnswer_IdAndAnswer(user1_id: Long, user2_id: Long, answer: AnswerOptions) : List<FriendshipProjection>

    //Returns a friendShip with the specified users IDs
    fun findFriendshipByUserAsk_IdAndUserAnswer_Id(user1_id: Long, user2_id: Long) : Optional<Friendship>

    //fun findFriendsByMessagesIsNotNullAndUser1_IdOrUser2_IdAndAnswer(user1_id: Long, user2_id: Long, answer: AnswerOptions): List<Friendship>//todo List<FriendsChatProjection>
    //fun existsFriendsByUser1_IdOrUser2_Id(user1_id: Long, user2_id: Long) : Boolean

}