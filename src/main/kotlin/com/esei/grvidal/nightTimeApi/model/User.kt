package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

@Entity
class User(
        var name: String,
        @Column(unique = true)
        var nickname: String,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}