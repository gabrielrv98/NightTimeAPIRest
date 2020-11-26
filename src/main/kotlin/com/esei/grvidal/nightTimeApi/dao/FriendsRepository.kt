package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Friends
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface FriendsRepository : JpaRepository<Friends, Long> {
    fun findFriendsByUser1_IdOrUser2_Id(user1_id: Long, user2_id: Long) : List<Friends>
    fun existsFriendsByUser1_IdOrUser2_Id(user1_id: Long, user2_id: Long) : Boolean

}