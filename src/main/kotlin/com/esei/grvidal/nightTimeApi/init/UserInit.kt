package com.esei.grvidal.nightTimeApi.init

import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.repository.DateCityRepository
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.services.UserService
import java.time.LocalDate

class UserInit(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val dateCityRepository: DateCityRepository
) {

    fun addUser(user : UserDTOInsert, daysInFuture: HashMap<Long,City>){

        userService.save(user)
        val userDates = userRepository.findByNickname(user.nickname).get()

        for( day in daysInFuture){
            dateCityRepository.save(
                DateCity(
                    nextDate = LocalDate.now().plusDays(day.key),
                    nextCity = day.value,
                    user = userDates
                )
            )
        }

    }
}