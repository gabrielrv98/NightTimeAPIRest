package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class UserService : IUserService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var userRepository: UserRepository



    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<UserProjection> {

            return userRepository.getAllBy()
    }


    /**
     * This will show one user, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws( NotFoundException::class)
    override fun loadProjection(idUser: Long): UserProjection {

        return userRepository.getUserById(idUser)
                .orElseThrow{ NotFoundException("Couldn't find the user with id $idUser") }

    }

    /**
     * This will show one user, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws( NotFoundException::class)
    override fun load(idUser: Long): User {

        return userRepository.findById(idUser)
                .orElseThrow{ NotFoundException("Couldn't find the user with id $idUser") }

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     * should return a user or its ID
     */
    override fun save(user: User): User {
            return userRepository.save(user)
    }



    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun remove(idUser: Long) {

        val user = loadProjection(idUser)
        userRepository.deleteById(user.getId())

    }

    @Throws(NotFoundException::class)
    private fun loadByNickname(nickname: String): User{
        return userRepository.findByNickname(nickname)
                .orElseThrow { NotFoundException("No users with name $nickname were found") }
    }

    @Throws(NotFoundException::class)
    override fun login(nickname: String, password: String): Boolean {
        val user = loadByNickname(nickname)
        return user.password == password
    }
}


















