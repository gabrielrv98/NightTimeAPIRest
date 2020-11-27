package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.model.SecretData
import com.esei.grvidal.nightTimeApi.model.User
import java.util.*

/**
 * DAO Interface for Bars
 */
interface IUserBusiness {

    //List all the user
    fun list(): List<User>

    //Show one user
    fun load(idUser: Long): User

    //Save a new user
    fun save(user: User): User
    fun saveSecretData(secretData: SecretData): SecretData

    //remove an user
    fun remove(idUser: Long)


    fun login(user: User, password: String): UUID

}