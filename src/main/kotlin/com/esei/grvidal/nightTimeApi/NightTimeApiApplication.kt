package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.repository.*
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.services.UserService
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDate
import java.time.LocalTime

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
    lateinit var userService: UserService

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

        var genericCity = City("Pontevedra","Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Coruña","Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Allariz","Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Lugo","Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Rivadavia","Spain")
        cityRepositories.save(genericCity)




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



        var bar = Bar(
            "Night",
            "NightOwner",
            "Rua cabeza de manzaneda",
            city = cityOu,
            mondaySchedule = null,
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = "12:00-22:00",
            thursdaySchedule = null,
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = "14:40-21:20",
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)


        var event = Event( "Oferta 2 x 1",date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)

        Event( "Musica de los 90",date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)



        bar = Bar(
            "Lokal",
            "Lokal Owner Garcia",
            "Praza Correxidor",
            city = cityOu,
            mondaySchedule = "12:00-22:00",
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = null,
            thursdaySchedule = "14:40-21:20",
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = null,
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)


        event = Event( "Oferta 2 x 1",date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)

        Event( "Fiesta de la espuma",date = LocalDate.now().plusDays(2), bar = bar)
        eventRepositories.save(event)

        Event( "Hoy cerrado por fiesta infantil, nos vemos gente",date = LocalDate.now().plusDays(2), bar = bar)
        eventRepositories.save(event)

        bar = Bar(
            "Studio 34",
            "Studio Owner Santiago",
            "Rua Concordia",
            city = cityOu,
            mondaySchedule = "12:00-22:00",
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = null,
            thursdaySchedule = "14:40-21:20",
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = null,
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)


        event = Event( "Musica en vivo",date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)
        event = Event( "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion",date = LocalDate.now().plusDays(3), bar = bar)
        eventRepositories.save(event)
        event = Event( "Copas a 3 euros",date = LocalDate.now().plusDays(3), bar = bar)
        eventRepositories.save(event)



        var user = UserDTOInsert(
                "Gabriel Rguez",
                "grvidal",
                "1234",
                "Hey there i'm using NightTime",
                email = "grvidal@esei.uvigo.es",
                birthdate = LocalDate.of(1998, 3, 14),

        )

        userService.save(user)
        val user1 = userRepositories.findByNickname(user.nickname).get()
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user1))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityOu, user = user1))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusWeeks(1).plusDays(3), nextCity = cityOu, user = user1))

        user = UserDTOInsert(
                "Nuria Sotelo",
                "pinkxnut",
                "passwordUser2",
                ".",
                "nuasotelo@gmail.com",
                birthdate = LocalDate.of(2001, 9, 17),


        )

        userService.save(user)
        val user2 = userRepositories.findByNickname(user.nickname).get()
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user2))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityOu, user = user2))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(3), nextCity = cityOu, user = user2))


        user = UserDTOInsert(
                "Irene Monterey",
                "monteRey",
                "passwordUser3",
                "Pues soy mas de playa",
                email = "ireneReina@hotmail.com",
                birthdate = LocalDate.of(1998, 4, 12)
        )

        userService.save(user)
        val user3 = userRepositories.findByNickname(user.nickname).get()
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user3))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(2), nextCity = cityOu, user = user3))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(3), nextCity = cityOu, user = user3))


        user = UserDTOInsert(
                "Jose Negro",
                "joseju",
                "passwordUser4",
                "Todo lo que se pueda decir es irrelevante",
                "joseNegro@gmail.com",
                birthdate = LocalDate.of(1990, 4, 25)
        )

        userService.save(user)
        val user4 = userRepositories.findByNickname(user.nickname).get()
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user4))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityVigo, user = user4))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(2), nextCity = cityVigo, user = user4))

        val friend1_2 =  Friendship(user1,user2)
        friend1_2.answer = AnswerOptions.YES
        friendshipRepositories.save(friend1_2)

        friendshipRepositories.save(Friendship(user3,user2))

        val friend4_1 =  Friendship(user4,user1)
        friend4_1.answer = AnswerOptions.YES
        friendshipRepositories.save(friend4_1)

        var friendship = friendshipRepositories.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id,user1.id)[0]
        var msg = Message("Hola nuria", LocalDate.now(), LocalTime.now(), friendship, friendship.userAsk)
        messageRepositories.save(msg)
        msg = Message("Adios Gabriel", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), friendship, friendship.userAnswer)
        messageRepositories.save(msg)

        friendship = friendshipRepositories.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id,user1.id)[1]
        msg = Message("Hola Josune", LocalDate.now(), LocalTime.now(), friendship, friendship.userAnswer)
        messageRepositories.save(msg)




        //val friends1w3 = Friendship(user1,user3)

        //friendshipRepositories!!.save(friends1w3)

        logger.info("Application ready to use")
    }
}



fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}

