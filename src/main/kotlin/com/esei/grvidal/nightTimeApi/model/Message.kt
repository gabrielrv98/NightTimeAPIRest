package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")
class Message(
    var text: String,
    var date: LocalDate,
    var hour: LocalTime,

    @ManyToOne
        @JoinColumn(name = "friends_id", nullable = false)
        var friendship: Friendship,

    @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        var user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}