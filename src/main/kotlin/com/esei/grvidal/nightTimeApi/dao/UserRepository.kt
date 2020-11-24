package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

}