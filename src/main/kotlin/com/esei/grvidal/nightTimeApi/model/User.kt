package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import javax.persistence.*

public const val nicknameLenght = 30

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")
@Table(name="User")
class User(
        var name: String,
        @Column(unique = true, length = 30)
        var nickname: String,
        var password: String,
        var state: String? = null,
        var email: String,
        @Column(name = "birth_date")
        var birthdate: LocalDate,


) {
    @OneToMany(mappedBy = "user",cascade = [CascadeType.REMOVE])
    var nextDates : List<DateCity>? = null

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    constructor(idUser: Long): this("","","","","",LocalDate.now()){
        id = idUser
    }
}
