package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarFullProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IBarService {

    //list all the bars on a city
    fun listByCity(cityId: Long, page: Int = 0, size: Int = 5): List<BarProjection>

    //Returns a Bar Entity
    @Throws(NotFoundException::class)
    fun load(idBar: Long): Bar

    //returns a class with the rest of the bars attributes
    @Throws(NotFoundException::class)
    fun getDetails(idBar: Long): BarDetailsProjection

    @Deprecated("used fot testing")
    fun getFullProjection(idBar: Long): BarFullProjection

    //Saves a new bar
    fun save(bar: Bar): Long

    //Updates a bar
    @Throws(NotFoundException::class)
    fun update(idBar: Long, barEdit: BarDTOEdit)

    //Removes a bar
    @Throws(ServiceException::class, NotFoundException::class)
    fun remove(idBar: Long)
}