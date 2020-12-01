package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.BarDTOInsert
import com.esei.grvidal.nightTimeApi.dto.CityDTO
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

    //List all the bars
    fun list(): List<BarProjection>

    @Throws( NotFoundException::class)
    fun getProjection(idBar: Long): BarProjection

    //Show one bar
    fun load(idBar: Long): Bar

    //Save a bar
    fun save(bar: Bar): Bar

    //remove a bar
    fun remove(idBar: Long)
    fun listByCity(cityId: Long): List<BarProjection>
    fun getDetails(idBar: Long): BarDetailsProjection
    fun getFullProjection(idBar: Long): BarFullProjection
}