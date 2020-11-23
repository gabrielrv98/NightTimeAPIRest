package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Bar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository, this will link our Controller with the business
 */
@Repository
interface BarRepository : JpaRepository<Bar, Long> {

}