package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.init.ChatInit
import com.esei.grvidal.nightTimeApi.init.MessageInit
import com.esei.grvidal.nightTimeApi.init.UserInit
import com.esei.grvidal.nightTimeApi.repository.*
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.services.UserService
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
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
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var cityRepository: CityRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var friendshipRepository: FriendshipRepository

    @Autowired
    lateinit var messageRepository: MessageRepository


    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    override fun run(vararg args: String?) {

        // Cities ------------------------------------------------------------------------------------------------------

        val cityOu = City("Ourense", "Spain")
        val cityVigo = City("Vigo", "Spain")

        cityRepository.save(cityOu)
        cityRepository.save(cityVigo)

        var genericCity = City("Pontevedra", "Spain")
        cityRepository.save(genericCity)

        genericCity = City("Coruña", "Spain")
        cityRepository.save(genericCity)

        genericCity = City("Allariz", "Spain")
        cityRepository.save(genericCity)

        genericCity = City("Lugo", "Spain")
        cityRepository.save(genericCity)

        genericCity = City("Rivadavia", "Spain")
        cityRepository.save(genericCity)


        // Bar with events ---------------------------------------------------------------------------------------------

        var bar = Bar(
            name = "Luxus",
            owner = "Lara Santas",
            address = "Rúa Arturo Pérez Serantes, 3, 32005 Ourense",
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
        eventRepository.save(event)

        event = Event("Pins gratis", date = LocalDate.now(), bar = bar)
        eventRepository.save(event)

        event = Event("Musica en vivo", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepository.save(event)

        event = Event(
            "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion",
            date = LocalDate.now().plusDays(3),
            bar = bar
        )
        eventRepository.save(event)

        event = Event("Copas a 3 euros", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepository.save(event)

        bar = Bar(
            name = "Patio andaluz",
            owner = "Aida Miguez",
            address = "Calle buenavista",
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
        eventRepository.save(event)

        event = Event("Copas a 3 euros", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepository.save(event)

        event = Event("Traemos al mejor DJ", date = LocalDate.now(), bar = bar)
        eventRepository.save(event)


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
        eventRepository.save(event)


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
        eventRepository.save(event)

        Event("Musica de los 90", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepository.save(event)



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


        event = Event("Oferta 2 x 1.", date = LocalDate.now().plusDays(1), bar = bar)
        eventRepository.save(event)

        event = Event("Fiesta de la espuma.", date = LocalDate.now().plusDays(2), bar = bar)
        eventRepository.save(event)

        event = Event("Hoy cerrado por fiesta infantil, se dará un nuevo aviso pronto.", date = LocalDate.now().plusDays(2), bar = bar)
        eventRepository.save(event)

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
        eventRepository.save(event)
        event = Event(
            "Hoy cerrado por defuncion, esperemos que todos se pongan mejor, gracias por su atencion.",
            date = LocalDate.now().plusDays(3),
            bar = bar
        )
        eventRepository.save(event)
        event = Event("Copas a 3 euros.", date = LocalDate.now().plusDays(3), bar = bar)
        eventRepository.save(event)



        bar = Bar(
            name = "Requiem",
            owner = "Nuria Sotelo",
            address = "Rua Concordia",
            description = "Un lugar libre para gente libre.",
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
                address = "Rua San Francisco",
                description = "Un lugar libre para gente libre.",
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







        // Users -------------------------------------------------------------------------------------------------------

        val userInit = UserInit(userService, userRepository, dateCityRepository)

        userInit.addUser(
            user = UserDTOInsert(
                // user grvidal
            "Gabriel Rguez",
            "grvidal",
            "1234",
            "Hey there i'm using NightTime",
            email = "grvidal@esei.uvigo.es"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityVigo),
                Pair(9, cityOu)
            )
        )

        val user1 = userRepository.findByNickname("grvidal").get()

        userInit.addUser(
            user = UserDTOInsert(
                // user pinkxnut
                "Nuria Sotelo",
                "pinkxnut",
                "passwordUser2",
                ".",
                "nuasotelo@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(3, cityOu)
            )
        )

        val user2 = userRepository.findByNickname("pinkxnut").get()
        userService.setUserPicture(user2.id, "/userpics/user_pinkxnut_2021-03-16_18-31-14-988.jpg")

        userInit.addUser(
            user = UserDTOInsert(
                // user santii810
                "Santi Gómez",
                "santii810",
                "santiSuperSecret",
                "Programando y coodirigiendo TFGs",
                "santii810@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(3, cityOu)
            )
        )


        val user3 = userRepository.findByNickname("santii810").get()
        userService.setUserPicture(user3.id, "/userpics/user_santii810.jpg")

        userInit.addUser(
            user = UserDTOInsert(
                "Maria Vidal",
                "mvittae",
                "Gabriel<3",
                "Que es \"Luxus\"",
                email = "mjvidal@hotmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(3, cityOu)
            )
        )
        val user4 = userRepository.findByNickname("mvittae").get()

        userInit.addUser(
            user = UserDTOInsert(
                "Jose Negro",
                "joseju",
                "passwordUser4",
                "Todo lo que se pueda decir es irrelevante",
                "joseNegro@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityVigo),
                Pair(3, cityVigo)
            )
        )
        val user5 = userRepository.findByNickname("joseju").get()


        userInit.addUser(
            user = UserDTOInsert(
                "Sara Domarco",
                "saraDom",
                "passwordUser5",
                "Cuando se acaba el covid??",
                "saraDom@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(3, cityOu)
            )
        )


        userInit.addUser(
            user = UserDTOInsert(
                "Manuel Costa",
                "costaNotCoffe",
                "passwordUser5",
                "Soy Manuel",
                "manuelm@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityOu)
            )
        )


        userInit.addUser(
            user = UserDTOInsert(
                "Juan Quintás",
                "juquint",
                "passwordUser5",
                "Fiesta ya!",
                "juanjuan@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityOu)
            )
        )

        userInit.addUser(
            user = UserDTOInsert(
                "Manuel Serrano",
                "UnSerrano",
                "passwordUser5",
                "Has visto mi serie?",
                "Manujamones@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityOu)
            )
        )
        userInit.addUser(
            user = UserDTOInsert(
                "Laura Keyclass",
                "soyMonisima",
                "passwordUser5",
                "Alguien toma algo?",
                "specialKey@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityOu)
            )
        )

        userInit.addUser(
            user = UserDTOInsert(
                "John Bay",
                "Mkrpf",
                "passwordUser5",
                "Trabajando",
                "jonhMkBay@gmail.com"
            ),
            daysInFuture = hashMapOf(
                Pair(0, cityOu),
                Pair(1, cityOu),
                Pair(2, cityOu)
            )
        )

        var friendsExtra : Friendship
        var user : User
        // Refill some extra users
        for( i in 0 .. 25) {
            userInit.addUser(
                user = UserDTOInsert(
                    "Laura Keyclass$i",
                    "soyMonisima$i",
                    "passwordUser5",
                    "Alguien toma algo?",
                    "specialKey@gmail.com"
                ),
                daysInFuture = hashMapOf(
                    Pair(0, cityOu),
                    Pair(1, cityOu),
                    Pair(2, cityOu)
                )
            )

            user = userRepository.findByNickname("soyMonisima$i").get()
            friendsExtra = Friendship(user,user1)
            friendsExtra.answer = AnswerOptions.YES
            friendshipRepository.save(friendsExtra)

        }







        // Friends -----------------------------------------------------------------------------------------------------

        var friendshipRelations = Friendship(user1, user2)
        friendshipRelations.answer = AnswerOptions.YES
        friendshipRepository.save(friendshipRelations)// accepted friendship

        friendshipRelations = Friendship(user3, user1)
        friendshipRelations.answer = AnswerOptions.YES
        friendshipRepository.save(friendshipRelations)// accepted friendship

        friendshipRelations = Friendship(user4, user1)
        friendshipRelations.answer = AnswerOptions.YES
        friendshipRepository.save(friendshipRelations)// accepted friendship

        friendshipRepository.save(Friendship(user5, user1)) // offered friendship







        // Chats -------------------------------------------------------------------------------------------------------

        ChatInit(
            friendshipRepository.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id, user1.id)
                .firstOrNull { friendship -> friendship.userAsk.id == user1.id && friendship.userAnswer.id == user2.id   },
            messageRepository,
            listOf(
                MessageInit("Hola Nuria", 0),
                MessageInit("Que tal?", 0),
                MessageInit("Todo bien que tal estas tu?", 1),
                MessageInit("Genial, tengo ganas de ir al cine a ver esa nueva peli", 0),
                MessageInit("Yo tambien, dicen que esta muy bien", 1),
                MessageInit("Además hace mucho que no vamos al cine", 1),
                MessageInit("Te veo a las 12, no llegues tarde!", 0),
            )
        ).saveData()

        ChatInit(
            friendshipRepository.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id, user1.id)
                .firstOrNull { friendship -> friendship.userAsk.id == user3.id && friendship.userAnswer.id == user1.id   },
            messageRepository,
            listOf(
                MessageInit("Santiii al final lograste conectarlo?", 0),
                MessageInit("Si, ahora todo funciona bien, no se muy bien por que fallaba", 1),
                MessageInit("Seria un bug", 1),
                MessageInit("Tu hiciste tu parte", 1),
                MessageInit("Claroo, deberiamos estar listos para pasar a produccion", 0),
                MessageInit("Perfecto, te veo el jueves a las 6", 1),
                MessageInit("A las 16*", 1),
            )
        ).saveData()

        ChatInit(
            friendshipRepository.findFriendsByUserAsk_IdOrUserAnswer_Id(user1.id, user1.id)
            .firstOrNull { friendship -> friendship.userAsk.id == user4.id && friendship.userAnswer.id == user1.id   },
            messageRepository,
            listOf(
                MessageInit("Hola, la aplicacion dice que el sabado sales, nos vamos encontes?", 0),
                MessageInit("Claro, estare donde siempre, fijo que nos vemos", 1),
                MessageInit("Me puedes dar esa copa que me debias si quires", 0),
                MessageInit("Nunca perdonas una eh, el primero en ser encontrado invita a un chupito", 1),
                MessageInit("Mira que eres rata!!!", 0),
                MessageInit("Buena suerte XD", 1)
            )
        ).saveData()

        logger.info("Application ready to use")
    }
}


fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}

