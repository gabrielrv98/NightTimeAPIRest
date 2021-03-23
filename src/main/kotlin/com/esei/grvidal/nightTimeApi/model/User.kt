package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import javax.persistence.*

const val nicknameLength = 30

@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
@Table(name = "User")
class User(
    var name: String,
    @Column(unique = true, length = 30)
    var nickname: String,
    var password: String,
    var state: String? = null,
    var email: String

    ) {

    constructor(idUser: Long) : this("", "", "", "", "") {
        id = idUser
    }

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    var nextDates: List<DateCity>? = null

    var picture: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0


}
