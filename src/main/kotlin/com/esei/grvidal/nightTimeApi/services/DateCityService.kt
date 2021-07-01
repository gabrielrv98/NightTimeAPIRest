package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
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


    override fun addDate(idUser: Long, dateCity: DateCityDTO): Long {
        if (!cityRepository.existsById(dateCity.nextCityId))
            throw NotFoundException("No city with id ${dateCity.nextCityId} found")

        if (dateCity.nextDate.isBefore(LocalDate.now()))
            throw ServiceException("Only dates after today ( ${LocalDate.now()} can be added")

        return dateCityRepository.save(
            DateCity(
                dateCity.nextDate,
                nextCity = City(dateCity.nextCityId),
                user = User(idUser)
            )
        ).id
    }
}



















