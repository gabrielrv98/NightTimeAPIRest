package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.projections.DateCityProjection
import com.esei.grvidal.nightTimeApi.projections.DateCityReducedProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface DateCityRepository : JpaRepository<DateCity, Long> {

    fun findAllByUser_IdAndNextCity_IdAndNextDateAfter(user_id: Long, nextCity_id: Long, nextDate: LocalDate): List<DateCityReducedProjection>

    fun findByUser_IdAndNextCity_IdAndNextDate(user_id: Long, nextCity_id: Long, nextDate: LocalDate): Optional<DateCityProjection>
}