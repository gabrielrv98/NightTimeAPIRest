package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.BarRepository
import com.esei.grvidal.nightTimeApi.repository.CityRepository
import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.dto.BarDTOInsert
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

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
    override fun getDetails(idBar: Long): BarDetailsProjection {
        return barRepository.getBarDetailsById(idBar)
            .orElseThrow { NotFoundException("No bar with id $idBar have been found") }


    }

    /**
     * This will show one bar, if not it will throw a NotFoundException
     */
    override fun load(idBar: Long): Bar {
        return barRepository.findById(idBar)
            .orElseThrow { NotFoundException("No bar with id $idBar has been found") }
    }

    /**
     * This will save a new bar
     */
    override fun save(bar: BarDTOInsert): Long {
        val city = cityRepository.findById(bar.cityId)
            .orElseThrow { NotFoundException("No city with id ${bar.cityId} has been found") }

        return barRepository.save(
            Bar(
                name = bar.name,
                owner = bar.owner,
                address = bar.address,
                description = bar.description,
                mondaySchedule = bar.mondaySchedule,
                tuesdaySchedule = bar.tuesdaySchedule,
                wednesdaySchedule = bar.wednesdaySchedule,
                thursdaySchedule = bar.thursdaySchedule,
                fridaySchedule = bar.fridaySchedule,
                saturdaySchedule = bar.saturdaySchedule,
                sundaySchedule = bar.sundaySchedule,
                city = city
            )
        ).id
    }

    /**
     * This will update a bar through [barEdit] and its non-null attributes
     */
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

            this.mondaySchedule = updateField(this.mondaySchedule, barEdit.mondaySchedule)
            this.tuesdaySchedule = updateField(this.tuesdaySchedule, barEdit.tuesdaySchedule)
            this.wednesdaySchedule = updateField(this.wednesdaySchedule, barEdit.wednesdaySchedule)
            this.thursdaySchedule = updateField(this.thursdaySchedule, barEdit.thursdaySchedule)
            this.fridaySchedule = updateField(this.fridaySchedule, barEdit.fridaySchedule)
            this.saturdaySchedule = updateField(this.saturdaySchedule, barEdit.saturdaySchedule)
            this.sundaySchedule = updateField(this.sundaySchedule, barEdit.sundaySchedule)
        }

        barRepository.save(barOriginal)
    }

    private fun updateField(original: String?, new: String?): String?{

        return if (new == null){  // if the parameter [new] is null, it won't update
            original
        }else{
            if ( new =="null") null// if the containing string is null, mondaySchedule will be updated as null
            else new  //else it will update with the right value
        }
    }

    /**
     * This will remove a bars through its [idBar], if it cant find it, it will throw a NotFoundException
     */
    override fun remove(idBar: Long) {

        barRepository.delete(
            load(idBar)
        )

    }


}


















