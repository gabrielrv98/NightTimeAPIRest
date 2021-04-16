package com.esei.grvidal.nightTimeApi.model

import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")
@Table(name = "friendship")
class Friendship(
    @ManyToOne
        @JoinColumn(name="user_Ask")
        var userAsk: User,

    @ManyToOne
        @JoinColumn(name="user_Answer")
        var userAnswer: User

) {
    @Enumerated(EnumType.STRING)
    var answer: AnswerOptions = AnswerOptions.NOT_ANSWERED

    @OneToMany(mappedBy = "friendship",cascade = [CascadeType.REMOVE])
    var messages : Set<Message> ? = null

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}


