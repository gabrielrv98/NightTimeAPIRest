package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.BarRepository
import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarFullProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class BarService : IBarService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var barRepository: BarRepository

    @Autowired
    lateinit var cityRepository: CityRepository


    /**
     * List all the bars in the given city [cityId]
     */
    override fun listByCity(cityId: Long, page: Int, size: Int): List<BarProjection> {
        return barRepository.findByCity_Id(
            city_id = cityId,
            pageable = PageRequest.of(page, size)
        )
    }

    /**
     * Returns the information that is not in [BarProjection] from the [Bar] with id [idBar]
     */
    @Throws(NotFoundException::class)
    override fun getDetails(idBar: Long): BarDetailsProjection {
        return barRepository.getBarDetailsById(idBar)
            .orElseThrow { NotFoundException("No bar with id $idBar have been found") }


    }

    /**
     * This will show one bar, if not it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun load(idBar: Long): Bar {
        return barRepository.findById(idBar)
            .orElseThrow { NotFoundException("No bar with id $idBar have been found") }
    }

    /**
     * This will save a new bar
     */
    override fun save(bar: Bar): Long {
        return barRepository.save(bar).id
    }

    /**
     * This will update a bar through [barEdit] and its non-null attributes
     */
    @Throws(NotFoundException::class)
    override fun update(idBar: Long, barEdit: BarDTOEdit) {

        //getting the old Bar
        val barOriginal = load(idBar)

        val cityEdit = barEdit.cityId?.let {newCity -> // if city has been updated
            cityRepository.findById(newCity)//get from DB
                .orElse(barOriginal.city)// If city Id doesn't exit old city is used instead
        }
            ?: barOriginal.city // if city hasn't been update


        //updating the bar
        barOriginal.apply {
            id = idBar
            name = barEdit.name ?: this.name
            owner = barEdit.owner ?: this.owner
            address = barEdit.address ?: this.address
            description = barEdit.description ?: this.description
            city = cityEdit

            // if the parameter [mondaySchedule] is null, it won't update
            this.mondaySchedule = if (barEdit.mondaySchedule == null)
                this.mondaySchedule
            else {
                // if the containing string is null, mondaySchedule will be updated as null
                if (barEdit.mondaySchedule == "null") null
                //else it will update with the right value
                else barEdit.mondaySchedule
            }

            this.tuesdaySchedule = if (barEdit.tuesdaySchedule == null)
                this.tuesdaySchedule
            else {
                if (barEdit.tuesdaySchedule == "null") null
                else barEdit.tuesdaySchedule
            }

            this.wednesdaySchedule = if (barEdit.wednesdaySchedule == null)
                this.wednesdaySchedule
            else {
                if (barEdit.wednesdaySchedule == "null") null
                else barEdit.wednesdaySchedule
            }

            this.thursdaySchedule = if (barEdit.thursdaySchedule == null)
                this.thursdaySchedule
            else {
                if (barEdit.thursdaySchedule == "null") null
                else barEdit.thursdaySchedule
            }

            this.fridaySchedule = if (barEdit.fridaySchedule == null)
                this.fridaySchedule
            else {
                if (barEdit.fridaySchedule == "null") null
                else  barEdit.fridaySchedule
            }

            this.saturdaySchedule = if (barEdit.saturdaySchedule == null)
                this.saturdaySchedule
            else {
                if (barEdit.saturdaySchedule == "null") null
                else  barEdit.saturdaySchedule
            }

            this.sundaySchedule = if (barEdit.sundaySchedule == null)
                this.sundaySchedule
            else {
                if (barEdit.sundaySchedule == "null") null
                else  barEdit.sundaySchedule
            }
        }

        barRepository.save(barOriginal)

    }

    /**
     * This will remove a bars through its [idBar], if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun remove(idBar: Long) {

        val bar = load(idBar)
        barRepository.delete(bar)

    }


}


















