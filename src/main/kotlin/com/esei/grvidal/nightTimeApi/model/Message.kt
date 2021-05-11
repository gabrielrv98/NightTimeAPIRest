package com.esei.grvidal.nightTimeApi.model

import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")
@Table(name ="message")
class Message(
    var text: String,
    var date: LocalDate = LocalDate.now(),
    var hour: LocalTime = LocalTime.now(),

    @ManyToOne
        @JoinColumn(name = "friendship_id", nullable = false)
        var friendship: Friendship,

    @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        var user: User
) {

    @Enumerated(EnumType.STRING)
    var readState: ReadState = ReadState.NOT_READ

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class ReadState{
    READ,
    NOT_READ
}