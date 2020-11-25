package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.dao.UserRepository
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class UserBusiness : IUserBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val userRepository: UserRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(BusinessException::class)
    override fun list(): List<User> {

        try {
            return userRepository!!.findAll()
                    /*//todo ver en el futuro
                    .onEach { user ->
                user.friends?.forEach { it.friends = null }
            }


                     */
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }


    /**
     * This will show one user, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun load(idUser: Long): User {
        val op: Optional<User>
        try {
            op = userRepository!!.findById(idUser)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idUser")
        }

        return op.get()

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(BusinessException::class)
    override fun save(user: User): User {

        try {
            return userRepository!!.save(user)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun remove(idUser: Long) {
        val op: Optional<User>

        try {
            op = userRepository!!.findById(idUser)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idUser")
        } else {

            try {
                userRepository!!.deleteById(idUser)
            } catch (e: Exception) {
                throw BusinessException(e.message)
            }
        }

    }
}


















