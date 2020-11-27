package com.esei.grvidal.nightTimeApi.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "secret_data")
class SecretData(

        var password: String,
        @OneToOne
        var user: User,

        @Column(unique = true)
        var uuid: UUID? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}