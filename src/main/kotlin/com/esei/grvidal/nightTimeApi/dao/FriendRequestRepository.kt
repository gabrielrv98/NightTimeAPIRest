package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.FriendRequest
import com.esei.grvidal.nightTimeApi.model.Friends
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface FriendRequestRepository : JpaRepository<FriendRequest, Long> {
    fun findFriendRequestByUserAsk_IdAndUserAnswer_Id(userAsk_id: Long, userAnswer_id: Long): Optional<FriendRequest>
    fun findFriendRequestByUserAnswer_Id(userAnswer_id: Long): List<FriendRequest>

}