package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.DateCity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface DateCityRepository : JpaRepository<DateCity, Long> {
    fun countAllByNextCity_IdAndNextDate(nextCity_id: Long, nextDate: LocalDate): Int
}