package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import javax.persistence.*

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")//todo no se como funcionara
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

        @ManyToMany
        @JoinTable(
                name = "friends",
                joinColumns = [JoinColumn(name = "user_1", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_2", referencedColumnName = "id")]
        )
        var friends: List<User>? = null

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}