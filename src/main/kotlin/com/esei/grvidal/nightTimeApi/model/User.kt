package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import javax.persistence.*

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")
class User(
        var name: String,
        @Column(unique = true, length = 20)
        var nickname: String,
        var password: String,
        var state: String? = null,
        var email: String,
        @Column(name = "birth_date")
        var birthdate: LocalDate,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "next_date_city_id")
        var dateCity: DateCity? = null

) {
    /*
    @OneToMany(mappedBy = "bar",cascade = [CascadeType.REMOVE])
    var friends : List<Event>? = null
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
