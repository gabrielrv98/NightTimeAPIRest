package com.esei.grvidal.nightTimeApi.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "next_date_city")
class DateCity(
        @Column(name = "next_date")
        var nextDate: LocalDate,

        @ManyToOne
        @JoinColumn(name = "city_id")
        var nextCity: City,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User,
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}