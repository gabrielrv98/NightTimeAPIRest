package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun getAllBy(): List<UserProjection>
    fun getUserById (id: Long): Optional<UserProjection>

    fun findByNickname(nickname: String): Optional<User>
}