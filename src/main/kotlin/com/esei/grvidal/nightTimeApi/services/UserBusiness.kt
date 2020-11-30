package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.SecretDataRepository
import com.esei.grvidal.nightTimeApi.dao.UserRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.SecretData
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.serviceInterface.IUserBusiness
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

    @Autowired
    val secretDataRepository: SecretDataRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<User> {

        try {
            return userRepository!!.findAll()
                    /*//todo ver en el futuro
                    .onEach { user ->
                user.friends?.forEach { it.friends = null }
            }


                     */
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    /**
     * This will show one user, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws( NotFoundException::class)//TODO COPIAR
    override fun load(idUser: Long): User {

        return userRepository!!.findById(idUser)
                .orElseThrow{ NotFoundException("Couldn't find the user with id $idUser") }

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(user: User): User {

        try {
            return userRepository!!.save(user)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
override fun saveSecretData(secretData: SecretData): SecretData {

        try {
            secretData.uuid = UUID.randomUUID()
            return secretDataRepository!!.save(secretData)

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(idUser: Long) {
        val op: Optional<User>

        try {
            op = userRepository!!.findById(idUser)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idUser")
        } else {

            try {
                userRepository!!.deleteById(idUser)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }

    @Throws(ServiceException::class, NotFoundException::class)
    override fun login(user: User, password: String): UUID {
        val op: Optional<SecretData>

        try {
            op = secretDataRepository!!.findDistinctFirstByUserAndPassword(user,password)

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("Credenciales no coinciden ")
        } else {
            with(op.get()){

                if (this.uuid == null) {

                    this.uuid = UUID.randomUUID()
                    secretDataRepository!!.save(this)
                }

                return this.uuid!!
            }


        }


    }
}


















