package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarFullProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface BarRepository : JpaRepository<Bar, Long> {
    //override fun findAll(): MutableList<Bar>
    fun getBarBy(): List<BarProjection>
    fun getFullBarById(id: Long): BarFullProjection
    fun getBarById(id: Long): BarProjection //Optional<BarProjection>
    fun getBarDetailsById(id: Long): BarDetailsProjection
    fun getBarsByCity_Id(city_id: Long): List<BarProjection>
    //fun getBarsByCity() : List<BarProjection>
}

