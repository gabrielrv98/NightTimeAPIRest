package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarProjection

/**
 * DAO Interface for Bars
 */
interface IBarService {

    //List all the bars
    fun list(): List<BarProjection>

    fun getProjection(idBar: Long): BarProjection

    //Show one bar
    fun load(idBar: Long): Bar

    //Save a new bar
    fun save(bar: Bar): Bar

    //remove a bar
    fun remove(idBar: Long)
}