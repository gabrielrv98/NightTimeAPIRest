package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
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

    //Returns all the FriendsShips where an user is involved and has a specific answer
    @Query(value = "SELECT f FROM Friendship f WHERE (f.userAsk.id = ?1 OR f.userAnswer.id = ?1) AND f.answer = 1 ")
    fun findFriendshipsFromUser(user_id: Long) : List<FriendshipProjection>

    //Returns a friendShip with the specified users IDs
    fun findFriendshipByUserAsk_IdAndUserAnswer_Id(userAsk_id: Long, userAnswer_id: Long) : Optional<Friendship>

    //Returns all the ids of the Friendships where the user is involved and there is any message
    @Query(value = "SELECT DISTINCT f FROM Friendship f INNER JOIN Message m ON f.id = m.friendship.id WHERE f.userAsk.id = ?1 OR f.userAnswer.id = ?1")
    fun getChatsWithMessages(idUser: Long): List<Friendship>

    @Query(value = "SELECT COUNT (T.total) FROM(" +
            " SELECT F.id, F.user_ask AS total FROM friendship AS F " +
            "JOIN next_date_city AS DT " +
            "ON F.user_answer = DT.user_id " +
            "WHERE F.user_ask = ?1" +
            "AND DT.city_id = ?2" +
            "AND DT.next_date = ?3" +
            " ) AS T",nativeQuery = true)
    fun getFriendsFromUserAskOnDateOriginal(idUser: Long, idCity: Long, date: LocalDate) : Int

    @Query(value = "SELECT COUNT(F.id) " +
            "FROM Friendship AS F JOIN DateCity AS DC on F.userAnswer.id = DC.user.id WHERE F.answer= 1 AND F.userAsk.id = ?1 AND DC.nextCity.id = ?2 AND DC.nextDate = ?3")
    fun getFriendsFromUserAskOnDate(idUser: Long, idCity: Long, date: LocalDate) : Int

    @Query(value = "SELECT COUNT(F.id) " +
            "FROM Friendship AS F JOIN DateCity AS DC on F.userAsk.id = DC.user.id WHERE F.answer= 1 AND F.userAnswer.id = ?1 AND DC.nextCity.id = ?2 AND DC.nextDate = ?3")
    fun getFriendsFromAnswerOnDate(idUser: Long, idCity: Long, date: LocalDate) : Int



}