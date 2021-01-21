package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.repository.DateCityRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IDateCityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class DateCityService : IDateCityService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var dateCityRepository: DateCityRepository


    @Autowired
    lateinit var cityRepository: CityRepository


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<DateCity> {

        try {
            return dateCityRepository.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    @Throws(ServiceException::class)
    override fun getTotalPeopleByDateAndCity(nextCity_id: Long, nextDate: LocalDate): Int {
        try {

            return dateCityRepository.countAllByNextCity_IdAndNextDate(nextCity_id, nextDate)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

    }

    override fun addDate(idUser: Long, dateCity: DateCityDTO): Long {
        if (!cityRepository.existsById(dateCity.nextCityId))
            throw NotFoundException("No city with id ${dateCity.nextCityId} found")
        return dateCityRepository.save(dateCity.toDateCity(idUser)).id
    }


    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(dateCity: DateCity): DateCity {

        try {
            return dateCityRepository.save(dateCity)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

}


fun DateCityDTO.toDateCity( userId: Long): DateCity {
    return DateCity(nextDate,
            nextCity = City(nextCityId),
            user = User(userId)
    )
}


















