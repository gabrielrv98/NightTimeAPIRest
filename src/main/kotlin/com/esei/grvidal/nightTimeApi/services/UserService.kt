package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toUser
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.repository.DateCityRepository
import com.esei.grvidal.nightTimeApi.security.UserPrincipal
import com.esei.grvidal.nightTimeApi.serviceInterface.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class UserService : IUserService , UserDetailsService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var cityRepository: CityRepository

    @Autowired
    lateinit var dateCityRepository: DateCityRepository


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
    @Throws(NotFoundException::class)
    override fun loadProjection(idUser: Long): UserProjection {

        return userRepository.getUserById(idUser)
                .orElseThrow { NotFoundException("Couldn't find the user with id $idUser") }

    }

    // UserDetailsService functions
    override fun loadUserByUsername(nickname: String ): UserDetails {
        val user = userRepository.findByNickname(nickname)
                .orElseThrow { UsernameNotFoundException("Couldn't find the user with id $nickname") }
        return UserPrincipal(user)

    }

    /**
     * This will show one user, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    private fun load(idUser: Long): User {

        return userRepository.findById(idUser)
                .orElseThrow { NotFoundException("Couldn't find the user with id $idUser") }

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     * should return a user or its ID
     */
    override fun save(user: UserDTOInsert): Long {
        return userRepository.save(user.toUser()).id
    }

    override fun update(idUser: Long, user: UserDTOEdit) {
        val userOriginal = load(idUser)
        userRepository.save(user.toUser(userOriginal))
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
    private fun loadByNickname(nickname: String): User {
        return userRepository.findByNickname(nickname)
                .orElseThrow { NotFoundException("No users with name $nickname were found") }
    }

    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    @Throws(NotFoundException::class)
    override fun login(nickname: String, password: String): Boolean {
        val user = loadByNickname(nickname)
        logger.info("UserService: login: password header $password // passwordDB: ${user.password} // same = "+ user.password.equals(password) )
        return user.password == password
    }


    @Throws(NotFoundException::class)
    override fun deleteDate(idUser: Long, idDate: Long): Boolean {
        val user = load(idUser)
        var delete = false

        if (user.nextDates != null) {
            val element = user.nextDates!!.filter { it.id == idDate }
            if (element.isNotEmpty()) {
                delete = true
                dateCityRepository.deleteById(idDate)
            }
        }

        return delete
    }

    override fun exists(idUser: Long): Boolean {
        return userRepository.findById(idUser).isPresent
    }


}

fun UserDTOEdit.toUser(user: User): User {
    return User(
            name ?: user.name,
            nickname = user.nickname,
            password ?: user.password,
            state,
            email ?: user.email,
            birthdate = user.birthdate
    ).apply { id = user.id }
}



















