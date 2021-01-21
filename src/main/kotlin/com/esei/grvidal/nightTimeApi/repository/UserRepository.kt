package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
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
    fun getUserById (id: Long): Optional<UserProjection>

    fun findByNickname(nickname: String): Optional<User>

    //SELECT COUNT(U.id) AS TOTAL_PEOPLE FROM User AS U INNER JOIN next_date_city AS DC ON DC.user_id = U.id WHERE DC.city_id = 1 AND DC.next_date = 2021-01-21
    @Query(value = "SELECT COUNT (U.id) AS TOTAL_PEOPLE FROM User AS U INNER JOIN DateCity AS DC ON DC.user.id = U.id WHERE DC.nextCity.id = ?1 AND DC.nextDate = ?2")
    fun getTotalOnDate(cityId: Long, date: LocalDate) : Int
}