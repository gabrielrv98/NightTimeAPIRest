package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Event

data class BarDTOInsert(
        val name: String,
        val owner: String,
        val address: String,
        val mondaySchedule: String? = null,
        val tuesdaySchedule: String? = null,
        val wednesdaySchedule: String? = null,
        val thursdaySchedule: String? = null,
        val fridaySchedule: String? = null,
        val saturdaySchedule: String? = null,
        val sundaySchedule: String? = null,
        val cityId: Long
) {

}