package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

/**
 * Entity of the Bar, this holds the data that the DB can save
 */
@Entity
@Table(name = "bar")
class Bar(
        var name: String,
        var jefe: String,
        var direccion: String,
        //@OnetoOne var sheduleId :Schedule
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0

}

@Entity
class Schedule(
        var monday: Boolean,
        var tuesday: Boolean,
        var wednesday: Boolean,
        var thursday: Boolean,
        var friday: Boolean,
        var saturday: Boolean,
        var sunday: Boolean,
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0
}