package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

/**
 * Repository, this will link Services with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {

    //Returns a projection of a Friendship
    fun findFriendshipById(id: Long): Optional<FriendshipProjection>

    //Returns all the FriendsShips where an user is involved
    fun findFriendsByUserAsk_IdOrUserAnswer_Id(user1_id: Long, user2_id: Long) : List<Friendship>

    //Returns all the FriendsShips where an user is involved and has a YES answer
    @Query(value = "SELECT f FROM Friendship f WHERE (f.userAsk.id = ?1 OR f.userAnswer.id = ?1) AND f.answer = com.esei.grvidal.nightTimeApi.utils.AnswerOptions.YES ")
    fun findFriendshipsFromUser(user_id: Long) : List<FriendshipProjection>

    //Returns a friendShip with the specified users IDs
    fun findFriendshipByUserAsk_IdAndUserAnswer_Id(userAsk_id: Long, userAnswer_id: Long) : Optional<FriendshipProjection>

    //Returns all the Friendships where the user is involved and there is any message
    @Query(value = "SELECT DISTINCT f FROM Friendship f INNER JOIN Message m ON f.id = m.friendship.id WHERE f.userAsk.id = ?1 OR f.userAnswer.id = ?1 ORDER BY m.date ASC")
    fun getChatsWithMessages(idUser: Long): List<Friendship>

    /**
     *  Returns the number of friends of user_ask with id [idUser] who checked the date [date] in the city with id [idCity]
     */
    @Query(value = "SELECT COUNT(F.id) " +
            "FROM Friendship AS F JOIN DateCity AS DC on F.userAnswer.id = DC.user.id WHERE F.answer= com.esei.grvidal.nightTimeApi.utils.AnswerOptions.YES AND F.userAsk.id = ?1 AND DC.nextCity.id = ?2 AND DC.nextDate = ?3"
    )
    fun getCountFriendsFromUserAskOnDate(idUser: Long, idCity: Long, date: LocalDate) : Int

    /**
     *  Returns the number of friends of user_answer with id [idUser] who checked the date [date] in the city with id [idCity]
     */
    @Query(value = "SELECT COUNT(F.id) " +
            "FROM Friendship AS F JOIN DateCity AS DC on F.userAsk.id = DC.user.id WHERE F.answer= com.esei.grvidal.nightTimeApi.utils.AnswerOptions.YES AND F.userAnswer.id = ?1 AND DC.nextCity.id = ?2 AND DC.nextDate = ?3"
    )
    fun getCountFriendsFromAnswerOnDate(idUser: Long, idCity: Long, date: LocalDate) : Int

    /**
     *  Returns a list of users that have a friendship with client with id [idUser] and have checked the date [date] in the city with id [idCity],
     *  allows pagination
     */
    @Query(
        value = "SELECT DISTINCT F FROM Friendship AS F JOIN DateCity AS DC on F.userAsk.id = DC.user.id JOIN DateCity AS DC2 on F.userAnswer.id = DC2.user.id " +
                "WHERE F.answer= com.esei.grvidal.nightTimeApi.utils.AnswerOptions.YES AND ( DC.nextCity.id = ?2 AND DC.nextDate = ?3 AND F.userAnswer.id = ?1 ) OR ( DC2.nextCity.id = ?2 AND DC2.nextDate = ?3 AND F.userAsk.id = ?1 ) "
    )
    fun getFriendsOnDate(idUser: Long, idCity: Long, date: LocalDate, pageable: Pageable) : List<FriendshipProjection>

    /**
     * Returns a list of [Friendship] where the user must answer and the answer is undefined
     */
    @Query(value = "SELECT f FROM Friendship f WHERE  f.userAnswer.id = ?1 AND f.answer = com.esei.grvidal.nightTimeApi.utils.AnswerOptions.NOT_ANSWERED")
    fun getFriendshipsRequest(user_id: Long) : List<FriendshipProjection>

    @Query(value = "SELECT COUNT (f) FROM Friendship f WHERE  f.userAnswer.id = ?1 AND f.answer = com.esei.grvidal.nightTimeApi.utils.AnswerOptions.NOT_ANSWERED")
    fun getCountAllByUserAnswerAndAnswer(user_id: Long): Int



}