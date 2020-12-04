package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.DateCityRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.DateCity
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
    val dateCityRepository: DateCityRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<DateCity> {

        try {
            return dateCityRepository!!.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    @Throws(ServiceException::class)
    override fun getTotalPeopleByDateAndCity(nextCity_id: Long, nextDate: LocalDate): Int {
        try {

            return dateCityRepository!!.countAllByNextCity_IdAndNextDate(nextCity_id, nextDate)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(dateCity: DateCity): DateCity {

        try {
            return dateCityRepository!!.save(dateCity)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


}


















