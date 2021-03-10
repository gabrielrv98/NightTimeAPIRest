package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

/**
 * RelationShip between a bar and a photo, this class holds the directory of each picture
 * @param dir is the directory of the Photo
 */

@Entity
class PhotoURL(
        var dir: String,

        @ManyToOne
        @JoinColumn(name = "bar_id")
        var bar: Bar,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0
}