package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.DateCityRepository
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.model.DateCity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class DateCityBusiness : IDateCityBusiness {

    /**
     *Dependency injection with autowired
     */


    @Autowired
    val dateCityRepository: DateCityRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(BusinessException::class)
    override fun list(): List<DateCity> {

        try {
            return dateCityRepository!!.findAll()
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }

    @Throws(BusinessException::class)
    override fun getTotalPeopleByDateAndCity(nextCity_id: Long, nextDate: LocalDate): Int {
        try {

            return dateCityRepository!!.countAllByNextCity_IdAndNextDate(nextCity_id, nextDate)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(BusinessException::class)
    override fun save(dateCity: DateCity): DateCity {

        try {
            return dateCityRepository!!.save(dateCity)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }


}


















