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

        // Cities -----------------------------

        val cityOu = City("Ourense", "Spain")
        val cityVigo = City("Vigo", "Spain")

        cityRepositories.save(cityOu)
        cityRepositories.save(cityVigo)

        var genericCity = City("Pontevedra", "Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Coruña", "Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Allariz", "Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Lugo", "Spain")
        cityRepositories.save(genericCity)

        genericCity = City("Rivadavia", "Spain")
        cityRepositories.save(genericCity)


        // Bar with events ---------------------

        var bar = Bar(
            name = "Luxus",
            owner = "Lara Santas",
            address = "Mercedes 2º",
            description = "Entradas a 6$ pero si llegas antes de las 2 es gratis",
            city = cityOu,
            mondaySchedule = "11:00-20:30",
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = "12:00-22:00",
            thursdaySchedule = "12:00-22:00",
            fridaySchedule = null,
            saturdaySchedule = "14:40-21:20"
        )

        bar = barRepository.save(bar)
        bar.photos = listOf(
            PhotoURL("/barpics/bar${bar.id}_0.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_1.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_2.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_3.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_4.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_5.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_6.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_7.jpg", bar)
        )

        barRepository.save(bar)

        var event = Event("copas a menos de 1 euro", date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)

        event = Event("Fardos gratis", date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)

        event = Event("Musica en vivo", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)

        event = Event(
            "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion",
            date = LocalDate.now().plusDays(3),
            bar = bar
        )
        eventRepositories.save(event)

        event = Event("Copas a 3 euros", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepositories.save(event)

        bar = Bar(
            name = "Patio andaluz",
            owner = "Aida Miguez",
            address = "Calle turbia",
            description = "Un buen lugar para charlar, conocer gente y disfrutar de la vida reunido de buenas compañias",
            city = cityOu,
            mondaySchedule = "12:00-20:30",
            tuesdaySchedule = "12:00-20:30",
            wednesdaySchedule = null,
            thursdaySchedule = "17:00-22:00",
            fridaySchedule = null,
            saturdaySchedule = "14:40-21:20"
        )
        bar = barRepository.save(bar)
        bar.photos = listOf(
            PhotoURL("/barpics/bar${bar.id}_0.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_1.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_2.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_3.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_4.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_5.jpg", bar),
            PhotoURL("/barpics/bar${bar.id}_6.jpg", bar)
        )
        barRepository.save(bar)



        event = Event(
            "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion",
            date = LocalDate.now().plusDays(3),
            bar = bar
        )
        eventRepositories.save(event)

        event = Event("Copas a 3 euros", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepositories.save(event)

        event = Event("No lo tenemos muy claro", date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)


        bar = Bar(
            name = "Faro de Vigo",
            owner = "Juan Miranda",
            address = "Calle Principe",
            description = "Somos el mejor lugar para verlo todo",
            city = cityVigo,
            mondaySchedule = "11:00-20:30",
            tuesdaySchedule = null,
            wednesdaySchedule = "12:00-22:00",
            thursdaySchedule = "12:00-22:00",
            fridaySchedule = null,
            saturdaySchedule = "14:40-21:20"
        )
        barRepository.save(bar)


        event = Event("Todo barato", date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)


        bar = Bar(
            name = "Night",
            owner = "NightOwner",
            address = "Rua cabeza de manzaneda",
            description = "Ven y pasatelo como si volvieses a tener 15",
            city = cityOu,
            mondaySchedule = "12:00-22:00",
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = "12:00-22:00",
            thursdaySchedule = null,
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = "14:40-21:20",
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)


        event = Event("Oferta 2 x 1", date = LocalDate.now(), bar = bar)
        eventRepositories.save(event)

        Event("Musica de los 90", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)



        bar = Bar(
            "Lokal",
            "Mario Garcia",
            "Praza Correxidor",
            description = "Un lokal para escuchar rock",
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


        event = Event("Oferta 2 x 1", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)

        event = Event("Fiesta de la espuma", date = LocalDate.now().plusDays(2), bar = bar)
        eventRepositories.save(event)

        event = Event("Hoy cerrado por fiesta infantil, nos vemos gente", date = LocalDate.now().plusDays(2), bar = bar)
        eventRepositories.save(event)

        bar = Bar(
            name = "Studio 34",
            owner = "Studio Owner Santiago",
            address = "Rua Concordia",
            description = "Un lugar libre para gente libre",
            city = cityOu,
            mondaySchedule = null,
            tuesdaySchedule = "11:00-20:30",
            wednesdaySchedule = null,
            thursdaySchedule = "14:40-21:20",
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = null,
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)


        event = Event("Musica en vivo", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepositories.save(event)
        event = Event(
            "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion",
            date = LocalDate.now().plusDays(3),
            bar = bar
        )
        eventRepositories.save(event)
        event = Event("Copas a 3 euros", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepositories.save(event)



        bar = Bar(
            name = "Requiem",
            owner = "Nuria Sotelo",
            address = "Rua Concordia",
            description = "Un lugar libre para gente libre",
            city = cityOu,
            mondaySchedule = "12:00-22:00",
            tuesdaySchedule = null,
            wednesdaySchedule = "11:00-20:30",
            thursdaySchedule = "14:40-21:20",
            fridaySchedule = "11:00-20:30",
            saturdaySchedule = null,
            sundaySchedule = "09:30-21:30"
        )
        barRepository.save(bar)

        for (i in 0..15) {
            bar = Bar(
                name = "Bar$i",
                owner = "Nuria Sotelo",
                address = "Rua Concordia",
                description = "Un lugar libre para gente libre",
                city = cityOu,
                mondaySchedule = "12:00-22:00",
                tuesdaySchedule = null,
                wednesdaySchedule = "11:00-20:30",
                thursdaySchedule = "14:40-21:20",
                fridaySchedule = "11:00-20:30",
                saturdaySchedule = null,
                sundaySchedule = "09:30-21:30"
            )
            barRepository.save(bar)
        }

        // Users --------------------------

        var user = UserDTOInsert(
            // user grvidal
            "Gabriel Rguez",
            "grvidal",
            "1234",
            "Hey there i'm using NightTime",
            email = "grvidal@esei.uvigo.es",
            birthdate = LocalDate.of(1998, 3, 14)

        )
        userService.save(user)
        val user1 = userRepositories.findByNickname(user.nickname).get()
        userService.setUserPicture(user1.id, "/userpics/user_grvidal.jpg")

        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user1))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityOu, user = user1))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(2), nextCity = cityVigo, user = user1))
        dateCityRepository.save(
            DateCity(
                nextDate = LocalDate.now().plusWeeks(1).plusDays(3),
                nextCity = cityOu,
                user = user1
            )
        )

        user = UserDTOInsert( // user pinkxnut
            "Nuria Sotelo",
            "pinkxnut",
            "passwordUser2",
            ".",
            "nuasotelo@gmail.com",
            birthdate = LocalDate.of(2001, 9, 17)
        )

        userService.save(user)
        val user2 = userRepositories.findByNickname(user.nickname).get()

        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user2))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityOu, user = user2))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(3), nextCity = cityOu, user = user2))


        user = UserDTOInsert(
            // user santii810
            "Santi Gómez",
            "santii810",
            "santiSuperSecret",
            "Programando y coodirigiendo TFGs",
            "santii810@gmail.com",
            birthdate = LocalDate.of(2001, 9, 17),
        )

        userService.save(user)
        val user3 = userRepositories.findByNickname(user.nickname).get()
        userService.setUserPicture(user3.id, "/userpics/user_santii810.jpg")

        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user3))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityOu, user = user3))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(3), nextCity = cityOu, user = user3))


        user = UserDTOInsert( // user monteRey
            "Maria Vidal",
            "mvittae",
            "Gabriel<3",
            "Que es \"Luxus\"",
            email = "mjvidal@hotmail.com",
            birthdate = LocalDate.of(1988, 3, 4)
        )

        userService.save(user)
        val user4 = userRepositories.findByNickname(user.nickname).get()

        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user4))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(2), nextCity = cityOu, user = user4))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(3), nextCity = cityOu, user = user4))


        user = UserDTOInsert(
            "Jose Negro",
            "joseju",
            "passwordUser4",
            "Todo lo que se pueda decir es irrelevante",
            "joseNegro@gmail.com",
            birthdate = LocalDate.of(1990, 4, 25)
        )

        userService.save(user)
        val user5 = userRepositories.findByNickname(user.nickname).get()

        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user5))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(1), nextCity = cityVigo, user = user5))
        dateCityRepository.save(DateCity(nextDate = LocalDate.now().plusDays(2), nextCity = cityVigo, user = user5))


        // Friends -----------------------

        val friend1_2 = Friendship(user1, user2)
        friend1_2.answer = AnswerOptions.YES
        friendshipRepositories.save(friend1_2)// accepted friendship

        val friend1_3 = Friendship(user3, user1 )
        friend1_3.answer = AnswerOptions.YES
        friendshipRepositories.save(friend1_3)// accepted friendship

        friendshipRepositories.save(Friendship(user3, user2)) // offered friendship
        friendshipRepositories.save(Friendship(user4, user1)) // offered friendship

        val friend4_1 = Friendship(user4, user1)
        friend4_1.answer = AnswerOptions.YES
        friendshipRepositories.save(friend4_1)// accepted friendship

        // Chats -----------------------

        var friendship = friendshipRepositories.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id, user1.id)[0]
        var msg = Message("Hola nuria", LocalDate.now(), LocalTime.now(), friendship, friendship.userAsk)
        messageRepositories.save(msg)
        msg = Message(
            "Adios Gabriel",
            LocalDate.now().plusDays(1),
            LocalTime.now().plusHours(1),
            friendship,
            friendship.userAnswer
        )
        messageRepositories.save(msg)

        friendship = friendshipRepositories.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id, user1.id)[1]
        msg = Message("Hola Santii", LocalDate.now(), LocalTime.now(), friendship, friendship.userAnswer)
        messageRepositories.save(msg)


        //val friends1w3 = Friendship(user1,user3)

        //friendshipRepositories!!.save(friends1w3)

        logger.info("Application ready to use")
    }
}


fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}

