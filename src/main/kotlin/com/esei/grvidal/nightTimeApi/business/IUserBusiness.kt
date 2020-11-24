package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.User

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

    //remove an user
    fun remove(idUser: Long)
}