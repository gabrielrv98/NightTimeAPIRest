package com.esei.grvidal.nightTimeApi.model

import java.time.LocalDate
import javax.persistence.*

/**
 * Entity of the Bar, this holds the data that the DB can save
 * @param owner is the owner of the bar
 * @param address is the address of the bar
 */

@Entity
@Table(name = "event")
class Event(
        var description: String,
        var date: LocalDate? = null,
        @ManyToOne
        @JoinColumn(name = "bar_id")
        var bar: Bar? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0
}