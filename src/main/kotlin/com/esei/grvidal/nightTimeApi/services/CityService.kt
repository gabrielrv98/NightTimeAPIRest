package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.serviceInterface.ICityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
    lateinit var cityRepository: CityRepository


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<City> {
        return cityRepository.findAll()

    }


    /**
     * This will show one City, if not, will throw a BusinessException or 
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun load(idCity: Long): City {

        return cityRepository.findById(idCity)
                .orElseThrow { NotFoundException("No se encontro la ciudad con el id $idCity") }
    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(city: City): City {
        return cityRepository.save(city)
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun remove(idCity: Long) {
        val bar = load(idCity)
        cityRepository.delete(bar)
    }
}


















