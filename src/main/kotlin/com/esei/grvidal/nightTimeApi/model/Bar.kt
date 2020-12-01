package com.esei.grvidal.nightTimeApi.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*



/**
 * Entity of the Bar, this holds the data that the DB can save
 * @param owner is the owner of the bar
 * @param address is the address of the bar
 *
 * //TODO LOOK https://www.baeldung.com/jpa-cascade-types
 * https://stackoverflow.com/questions/7197181/jpa-unidirectional-many-to-one-and-cascading-delete
 *
 */
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator::class,
        property = "id")//todo no se como funcionara
@Table(name = "bar")
class Bar(
        var name: String,
        var owner: String,
        var address: String,

        var mondaySchedule : String? = null,
        var tuesdaySchedule : String? = null,
        var wednesdaySchedule : String? = null,
        var thursdaySchedule : String? = null,
        var fridaySchedule : String? = null,
        var saturdaySchedule : String? = null,
        var sundaySchedule : String? = null,

        @ManyToOne//(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "city_id")
        var city: City,

) {
    
    @OneToMany(mappedBy = "bar",cascade = [CascadeType.REMOVE])
    var events : List<Event>? = null

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}