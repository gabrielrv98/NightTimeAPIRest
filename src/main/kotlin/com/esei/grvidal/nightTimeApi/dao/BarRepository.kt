package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Bar
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
    fun getBarById(id: Long): BarProjection
}

