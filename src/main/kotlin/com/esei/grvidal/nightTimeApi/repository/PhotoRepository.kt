package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.PhotoURL
import com.esei.grvidal.nightTimeApi.projections.PhotoProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 *
 */

@Repository
interface PhotoRepository : JpaRepository<PhotoURL, Long> {

    fun findByBar_Id(bar_id: Long, pageable: Pageable): List<PhotoProjection>
}

