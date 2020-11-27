package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.SecretData
import com.esei.grvidal.nightTimeApi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface SecretDataRepository : JpaRepository<SecretData, Long> {
    fun findDistinctFirstByUserAndPassword(user: User, password: String) : Optional<SecretData>
}