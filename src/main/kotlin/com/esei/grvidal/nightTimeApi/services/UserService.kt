package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toUser
import com.esei.grvidal.nightTimeApi.encryptation.Encoding
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.model.nicknameLength
import com.esei.grvidal.nightTimeApi.projections.DateCityReducedProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.repository.DateCityRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
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

    @Autowired
    lateinit var dateCityRepository: DateCityRepository

    //val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!


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

    /**
     * Gets a city id and a date [dateCityDTO] and returns the list of the dates selected by
     * the user in that city after the date
     *
     * @param idUser Id of the user in the database
     * @param dateCityDTO date and city id
     *
     */
    override fun loadUserDatesList(idUser: Long, dateCityDTO: DateCityDTO): List<DateCityReducedProjection> {

        return dateCityRepository.findAllByUser_IdAndNextCity_IdAndNextDateAfter(idUser,dateCityDTO.nextCityId,dateCityDTO.nextDate)
    }

    /**
     * Try to load an user by its ID, if there is no user with that id it will throw a BusinessException or
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

        //checking user constraints
        if (user.nickname.length > nicknameLength)
            throw ServiceException("User's nickname is too long")

        //Encrypting password
        user.password = Encoding.encrypt(
            strToEncrypt = user.password,
            secret_key = user.nickname
        )

        try{
            return userRepository.save(user.toUser()).id

        }catch (e: DataIntegrityViolationException){
            throw ServiceException("User with nickname ${user.nickname} already exists" )
        }
    }

    override fun update(idUser: Long, user: UserDTOEdit) {
        val userOriginal = load(idUser)
        userRepository.save(user.toUser(userOriginal))
    }


    /**
     * This will remove an user through its id
     * if [idUser] doesn't exist NotFoundException will be thrown
     */

    override fun remove(idUser: Long) {

        if (!exists(idUser)) throw NotFoundException("Couldn't find the user with id $idUser")
        userRepository.deleteById(idUser)

    }

    @Throws(NotFoundException::class)
    private fun loadByNickname(nickname: String): User {
        return userRepository.findByNickname(nickname)
            .orElseThrow { NotFoundException("No users with name $nickname were found") }
    }


    override fun login(nickname: String, password: String): Long {

        val user = loadByNickname(nickname)

        val decodedPassword = Encoding.decrypt(
            key = user.nickname,
            strToDecrypt = user.password
        )

        return if (decodedPassword == password) user.id
                else (-1)
    }


    override fun deleteDate(idUser: Long, dateCity: DateCityDTO): Long {

        val date = dateCityRepository.findByUser_IdAndNextCity_IdAndNextDate(idUser,dateCity.nextCityId,dateCity.nextDate)
            .orElseThrow { NotFoundException("No date selected with user id $idUser, cityId ${dateCity.nextCityId} and date ${dateCity.nextDate}") }

        val id = date.getId()
        dateCityRepository.deleteById(id)

        return id
    }

    override fun exists(idUser: Long): Boolean {
        return userRepository.findById(idUser).isPresent
    }

    override fun getTotal(dateCityDTO: DateCityDTO): Int {
        return userRepository.getTotalOnDate(
            cityId = dateCityDTO.nextCityId,
            date = dateCityDTO.nextDate
        )

    }

    override fun setUserPicture(id: Long, src: String?) {
        val user = load(id)
        user.picture = src
        userRepository.save(user)
    }

    /**
     * Checks if the user has any URL file associated, and if it was null rewrites it
     * if it wasn't null there is no need to rewrite since new picture has the same name as the old one
     */
    override fun setNewPicture(idUser: Long) {
        val user = load(idUser)
        if(user.picture == null) {
            user.picture = "/userpics/user_${user.nickname}"
            userRepository.save(user)
        }
    }


    override fun getPicture(id: Long): String? {
        return load(id).picture
    }

}





















