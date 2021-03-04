package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.City
import java.util.*
import kotlin.jvm.Throws

data class BarDTOInsert(
        val name: String,
        val owner: String,
        val address: String,
        val description: String? = null,
        val mondaySchedule: String? = null,
        val tuesdaySchedule: String? = null,
        val wednesdaySchedule: String? = null,
        val thursdaySchedule: String? = null,
        val fridaySchedule: String? = null,
        val saturdaySchedule: String? = null,
        val sundaySchedule: String? = null,
        val cityId: Long
)

@Throws(NotFoundException::class)
fun BarDTOInsert.toBar(city: City): Bar {

    return Bar(
            name = this.name,
            owner = this.owner,
            address = this.address,
            description = this.description,
            mondaySchedule = this.mondaySchedule,
            tuesdaySchedule = this.tuesdaySchedule,
            wednesdaySchedule = this.wednesdaySchedule,
            thursdaySchedule = this.thursdaySchedule,
            fridaySchedule = this.fridaySchedule,
            saturdaySchedule = this.saturdaySchedule,
            sundaySchedule = this.sundaySchedule,
            city = city
    )
}