package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

@Entity
@Table(name = "city")
class City(
        var name: String,
        var country: String,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}