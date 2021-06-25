package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarFullProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface BarRepository : JpaRepository<Bar, Long>{

    //return all the bars that has the city in common
    fun findByCity_Id(city_id: Long, pageable: Pageable) : List<BarProjection>


    fun getBarDetailsById(id: Long): Optional<BarDetailsProjection>

}

