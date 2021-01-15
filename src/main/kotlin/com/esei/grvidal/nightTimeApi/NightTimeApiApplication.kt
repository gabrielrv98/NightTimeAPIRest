package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.repository.*
import com.esei.grvidal.nightTimeApi.model.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDate

@SpringBootApplication
@EnableJpaRepositories("com.esei.grvidal.nightTimeApi.repository")
@EntityScan("com.esei.grvidal.nightTimeApi.model")
class NightTimeApiApplication : CommandLineRunner {

    @Autowired
    lateinit var barRepository: BarRepository

    @Autowired
    lateinit var dateCityRepository: DateCityRepository

    @Autowired
    lateinit var eventRepositories: EventRepository

    @Autowired
    lateinit var cityRepositories: CityRepository

    @Autowired
    lateinit var userRepositories: UserRepository

    @Autowired
    lateinit var friendshipRepositories: FriendshipRepository

    @Autowired
    lateinit var messageRepositories: MessageRepository


    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    override fun run(vararg args: String?) {

        val cityOu = City("Ourense","Spain")
        val cityVigo = City("Vigo","Spain")

        cityRepositories.save(cityOu)
        cityRepositories.save(cityVigo)

        val bar1 = Bar(
                "Luxus",
                "Algun pijo",
                "Mercedes 2º",
                city = cityOu,
                mondaySchedule = "11:00-20:30",
                tuesdaySchedule = "11:00-20:30",
                wednesdaySchedule = "12:00-22:00",
                thursdaySchedule = "12:00-22:00",
                fridaySchedule = null,
                saturdaySchedule = "14:40-21:20"
        )

        barRepository.save(bar1)

        val bar2 = Bar(
                "Patio andaluz",
                "Aida",
                "Calle turbia",
                city = cityOu,
                mondaySchedule = "12:00-20:30",
                tuesdaySchedule = "12:00-20:30",
                wednesdaySchedule = null,
                thursdaySchedule = "17:00-22:00",
                fridaySchedule = null,
                saturdaySchedule = "14:40-21:20"
        )
        barRepository.save(bar2)


        val event1 = Event( "copas a menos de 1 euro",date = LocalDate.now(), bar = bar1)
        val event2 = Event( "Fardos gratis",date = LocalDate.now(), bar = bar1)
        val event3 = Event( "No lo tenemos muy claro",date = LocalDate.now(), bar = bar2)



        eventRepositories.save(event1)
        eventRepositories.save(event2)
        eventRepositories.save(event3)

        val bar3 = Bar(
                "Faro de Vigo",
                "Juan",
                "Calle Principe",
                city = cityVigo,
                mondaySchedule = "11:00-20:30",
                tuesdaySchedule = null,
                wednesdaySchedule = "12:00-22:00",
                thursdaySchedule = "12:00-22:00",
                fridaySchedule = null,
                saturdaySchedule = "14:40-21:20"
        )
        barRepository.save(bar3)


        val event4 = Event( "Todo barato",date = LocalDate.now(), bar = bar3)
        eventRepositories.save(event4)



        val user1 = User(
                "Gabriel Rguez",
                "grvidal",
                "passwordUser1",
                "Hey there i'm using NightTime",
                email = "grvidal@esei.uvigo.es",
                birthdate = LocalDate.of(1998, 3, 14),

        )

        userRepositories.save(user1)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user1))

        val user2 = User(
                "Nuria Sotelo",
                "pinkxnut",
                "passwordUser2",
                ".",
                "nuasotelo@gmail.com",
                birthdate = LocalDate.of(2001, 9, 17),


        )
        userRepositories.save(user2)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user2))


        val user3 = User(
                "Irene Monterey",
                "monteRey",
                "passwordUser3",
                "Pues soy mas de playa",
                email = "ireneReina@hotmail.com",
                birthdate = LocalDate.of(1998, 4, 12)
        )
        userRepositories.save(user3)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user3))


        val user4 = User(
                "Jose Negro",
                "joseju",
                "passwordUser4",
                "Todo lo que se pueda decir es irrelevante",
                "joseNegro@gmail.com",
                birthdate = LocalDate.of(1990, 4, 25)
        )
        userRepositories.save(user4)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityVigo, user = user4))

        friendshipRepositories.save(Friendship(user1,user2))

        friendshipRepositories.save(Friendship(user3,user2))

        //val friendship = friendshipRepositories.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id,user1.id)[0]


        //var msg = Message("Hola nuria", LocalDate.now(), LocalTime.now(), friendship, friendship.userAsk)

        //messageRepositories.save(msg)
        //msg = Message("Adios Gabriel", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), friendship, friendship.userAnswer)
        //messageRepositories.save(msg)

        //val friends1w3 = Friendship(user1,user3)

        //friendshipRepositories!!.save(friends1w3)

        logger.info("Application ready to use")
    }
}



fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}

