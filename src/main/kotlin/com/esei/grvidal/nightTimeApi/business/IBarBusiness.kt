package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.Bar

/**
 * DAO Interface for Bars
 */
interface IBarBusiness {

    //List all the bars
    fun list(): List<Bar>
    //Show one bar
    fun load(idBar: Long): Bar
    //Save a new bar
    fun save(bar: Bar): Bar
    //remove a bar
    fun remove(idBar: Long)
}