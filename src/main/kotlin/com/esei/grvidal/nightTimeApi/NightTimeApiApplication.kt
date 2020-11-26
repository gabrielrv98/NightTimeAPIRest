package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dao.*
import com.esei.grvidal.nightTimeApi.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDate
import java.time.LocalTime

@SpringBootApplication
@EnableJpaRepositories("com.esei.grvidal.nightTimeApi.dao")
@EntityScan("com.esei.grvidal.nightTimeApi.model")
class NightTimeApiApplication : CommandLineRunner {

    @Autowired
    val barRepository: BarRepository? = null

    @Autowired
    val eventRepositories: EventRepository? = null

    @Autowired
    val cityRepositories: CityRepository? = null

    @Autowired
    val userRepositories: UserRepository? = null

    @Autowired
    val friendsRepositories: FriendsRepository? = null

    @Autowired
    val messageRepositories: MessageRepository? = null

    override fun run(vararg args: String?) {

        val cityOu = City("Ourense","Spain")
        val cityVigo = City("Vigo","Spain")

        cityRepositories!!.save(cityOu)
        cityRepositories!!.save(cityVigo)

        val bar1 = Bar("Luxus", "Algun pijo", "Mercedes 2º",city = cityOu)

        bar1.schedule =  Schedule(monday = true, tuesday = false, wednesday = false,
                thursday = true, friday = false, saturday = true, sunday = true)

        barRepository!!.save(bar1)

        val bar2 = Bar("Patio andaluz", "Aida", "Calle turbia", city = cityOu)
        bar2.schedule = Schedule(monday = true, tuesday = false, wednesday = false,
                thursday = true, friday = false, saturday = true, sunday = true)
        barRepository!!.save(bar2)


        val event1 = Event( "copas a menos de 1 euro",date = LocalDate.now(), bar = bar1)
        val event2 = Event( "Fardos gratis", bar = bar1)
        val event3 = Event( "No lo tenemos muy claro", bar = bar2)



        eventRepositories!!.save(event1)
        eventRepositories!!.save(event2)
        eventRepositories!!.save(event3)

        val bar3 = Bar("Faro de Vigo", "Juan", "Calle Principe",cityVigo)
        bar3.schedule = Schedule(monday = false, tuesday = true, wednesday = true,
                thursday = false, friday = false, saturday = true, sunday = false)
        barRepository!!.save(bar3)


        val event4 = Event( "Todo barato",date = LocalDate.now(), bar = bar3)
        eventRepositories!!.save(event4)



        val user1 = User(
                "Gabriel Rguez",
                "grvidal",
                "Hey there i'm using NightTime",
                email = "grvidal@esei.uvigo.es",
                birthdate = LocalDate.of(1998, 3, 14),

        )

        user1.dateCity =  DateCity(nextDate = LocalDate.now(), nextCity = cityOu)
        val user2 = User(
                "Nuria Sotelo",
                "pinkxnut",
                ".",
                "nuasotelo@gmail.com",
                birthdate = LocalDate.of(2001, 9, 17),


        )
        user2.dateCity = DateCity(nextDate = LocalDate.of(2020, 10, 30), nextCity = cityOu)
        val user3 = User(
                "Irene Monterey",
                "monteRey",
                "Pues soy mas de playa",
                email = "ireneReina@hotmail.com",
                birthdate = LocalDate.of(1998, 4, 12)
        )
        user3.dateCity = DateCity(nextDate = LocalDate.of(2020, 10, 30), nextCity = cityVigo)

        val user4 = User(
                "Jose Negro",
                "joseju",
                "Todo lo que se pueda decir es irrelevante",
                "joseNegro@gmail.com",
                birthdate = LocalDate.of(1990, 4, 25)
        )

        userRepositories!!.save(user1)
        userRepositories!!.save(user2)
        userRepositories!!.save(user3)
        userRepositories!!.save(user4)

        friendsRepositories!!.save(Friends(user1,user2))
        friendsRepositories!!.save(Friends(user3,user2))



        val friends = friendsRepositories!!.findFriendsByUser1_IdOrUser2_Id(user1.id, user1.id)[0]
        var msg = Message("Hola nuria", LocalDate.now(), LocalTime.now(), friends, friends.user1)

        messageRepositories!!.save(msg)
        msg = Message("Adios Gabriel", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), friends, friends.user2)
        messageRepositories!!.save(msg)

        //val chat2 = Friends(user1,user3)

        //friendsRepositories!!.save(chat2)



    }
}



fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}
