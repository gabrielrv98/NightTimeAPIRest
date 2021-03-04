package com.esei.grvidal.nightTimeApi.dto

data class BarDTOEdit(
        val name: String?,
        val owner: String?,
        val address: String?,
        val description: String?,
        val mondaySchedule: String? = null,
        val tuesdaySchedule: String? = null,
        val wednesdaySchedule: String? = null,
        val thursdaySchedule: String? = null,
        val fridaySchedule: String? = null,
        val saturdaySchedule: String? = null,
        val sundaySchedule: String? = null,
        val cityId: Long?
)