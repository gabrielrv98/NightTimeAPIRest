package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserEditProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun getAllBy(): List<UserProjection>

    fun getUserById(id: Long): Optional<UserProjection>

    fun findUserById(id: Long): Optional<UserEditProjection>

    fun findUsersByNameContainsOrNicknameContains(
        name: String,
        nickname: String,
        pageable: Pageable
    ): List<UserProjection>

    fun countUsersByNameContainsOrNicknameContains(name: String, nickname: String): Int

    fun findByNickname(nickname: String): Optional<User>

    @Query(value = "SELECT COUNT (U.id) AS TOTAL_PEOPLE FROM User AS U INNER JOIN DateCity AS DC ON DC.user.id = U.id WHERE DC.nextCity.id = ?1 AND DC.nextDate = ?2")
    fun getTotalOnDate(cityId: Long, date: LocalDate): Int
}