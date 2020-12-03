package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.serviceInterface.ICityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class CityService : ICityService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val cityRepository: CityRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<City> {

        try {
            return cityRepository!!.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    /**
     * This will show one City, if not, will throw a BusinessException or 
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun load(idCity: Long): City {

        return cityRepository!!.findById(idCity).orElseThrow { NotFoundException("No se encontro la ciudad con el id $idCity") }
    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(City: City): City {

        try {
            return cityRepository!!.save(City)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(idCity: Long) {
        val op: Optional<City>

        try {
            op = cityRepository!!.findById(idCity)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idCity")
        } else {

            try {
                cityRepository!!.deleteById(idCity)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }
}


















