package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarFullProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface BarRepository : JpaRepository<Bar, Long> {
    @Deprecated("only testing")
    fun getFullBarById(id: Long): BarFullProjection

    //return all the bars that has the city in common
    fun getBarsByCity_Id(city_id: Long): List<BarProjection>

    fun getBarDetailsById(id: Long): Optional<BarDetailsProjection>

}

