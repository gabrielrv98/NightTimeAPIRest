package com.esei.grvidal.nightTimeApi.model

import sun.text.normalizer.CharTrie
import java.time.LocalDate
import javax.persistence.*

@Entity
class User(
        var name: String,
        @Column(unique = true, length = 20)
        var nickname: String,
        var state: String? = null,
        @Column(name = "next_date")
        var nextDate: LocalDate? = null,

        @ManyToOne
        @JoinColumn(name = "city_id")
        var cityNextDate: City? = null,

        //@ManyToMany
        //var friends: List<User>? = listOf()
        //@OneToMany
        //var friends: List<User>? = listOf()
        ) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}