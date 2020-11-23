package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

/**
 * Entity of the Bar, this holds the data that the DB can save
 * @param owner is the owner of the bar
 * @param address is the address of the bar
 */
@Entity
@Table(name = "bar")
class Bar(
        var name: String,
        var owner: String,
        var address: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "schedule_id")
        var schedule: Schedule? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0

}

/**
 * Entity of the Schedule, this holds the data that the DB can save
 */
@Entity
@Table(name = "schedule")
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