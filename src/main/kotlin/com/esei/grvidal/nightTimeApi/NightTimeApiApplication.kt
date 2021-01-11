package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.exception.AuthenticationException
import com.esei.grvidal.nightTimeApi.repository.*
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.utlis.AuthName
import com.esei.grvidal.nightTimeApi.utlis.Cookies
import com.esei.grvidal.nightTimeApi.utlis.FormFields
import com.fasterxml.jackson.databind.SerializationFeature
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import java.time.LocalDate
import java.time.LocalTime


import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.*
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.*
import kotlin.collections.set

@SpringBootApplication
@EnableJpaRepositories("com.esei.grvidal.nightTimeApi.repository")
@EntityScan("com.esei.grvidal.nightTimeApi.model")
class NightTimeApiApplication : CommandLineRunner {

    @Autowired
    val barRepository: BarRepository? = null

    @Autowired
    lateinit var dateCityRepository: DateCityRepository

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


    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    override fun run(vararg args: String?) {

        val cityOu = City("Ourense","Spain")
        val cityVigo = City("Vigo","Spain")

        cityRepositories!!.save(cityOu)
        cityRepositories!!.save(cityVigo)

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

        barRepository!!.save(bar1)

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
        barRepository!!.save(bar2)


        val event1 = Event( "copas a menos de 1 euro",date = LocalDate.now(), bar = bar1)
        val event2 = Event( "Fardos gratis",date = LocalDate.now(), bar = bar1)
        val event3 = Event( "No lo tenemos muy claro",date = LocalDate.now(), bar = bar2)



        eventRepositories!!.save(event1)
        eventRepositories!!.save(event2)
        eventRepositories!!.save(event3)

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
        barRepository!!.save(bar3)


        val event4 = Event( "Todo barato",date = LocalDate.now(), bar = bar3)
        eventRepositories!!.save(event4)



        val user1 = User(
                "Gabriel Rguez",
                "grvidal",
                "passwordUser1",
                "Hey there i'm using NightTime",
                email = "grvidal@esei.uvigo.es",
                birthdate = LocalDate.of(1998, 3, 14),

        )

        userRepositories!!.save(user1)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user1))

        val user2 = User(
                "Nuria Sotelo",
                "pinkxnut",
                "passwordUser2",
                ".",
                "nuasotelo@gmail.com",
                birthdate = LocalDate.of(2001, 9, 17),


        )
        userRepositories!!.save(user2)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user2))


        val user3 = User(
                "Irene Monterey",
                "monteRey",
                "passwordUser3",
                "Pues soy mas de playa",
                email = "ireneReina@hotmail.com",
                birthdate = LocalDate.of(1998, 4, 12)
        )
        userRepositories!!.save(user3)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityOu, user = user3))


        val user4 = User(
                "Jose Negro",
                "joseju",
                "passwordUser4",
                "Todo lo que se pueda decir es irrelevante",
                "joseNegro@gmail.com",
                birthdate = LocalDate.of(1990, 4, 25)
        )
        userRepositories!!.save(user4)
        dateCityRepository.save(DateCity(nextDate = LocalDate.now(), nextCity = cityVigo, user = user4))

        friendsRepositories!!.save(Friends(user1,user2))
        friendsRepositories!!.save(Friends(user3,user2))



        val friends = friendsRepositories!!.findFriendsByUser1_IdOrUser2_Id(user1.id, user1.id)[0]
        var msg = Message("Hola nuria", LocalDate.now(), LocalTime.now(), friends, friends.user1)

        messageRepositories!!.save(msg)
        msg = Message("Adios Gabriel", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), friends, friends.user2)
        messageRepositories!!.save(msg)

        //val friends1w3 = Friends(user1,user3)

        //friendsRepositories!!.save(friends1w3)

        logger.info("Application ready to use")



    }


}

/*
old school ( en realidad es kotlin DSL )
@EnableWebSecurity
class KotlinSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http {
            httpBasic {}

            authorizeRequests {
                authorize("/api/v1/user/{id}", authenticated  )
                authorize("/**", permitAll)
            }
        }
    }


}

 */
 */

fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}
// fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)  didn't work at all

//Copied from  https://github.com/alekseinovikov/KtorAuthenticationExample
fun Application.module(testing: Boolean = false) {
    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Sessions) {
        cookie<UserIdPrincipal>(
                Cookies.AUTH_COOKIE,
                storage = SessionStorageMemory()
        ) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {
        //Configure Authentication with cookies
        session<UserIdPrincipal>(AuthName.SESSION) {
            challenge {
                // What to do if the user isn't authenticated
                throw AuthenticationException()
            }
            validate { session: UserIdPrincipal ->
                // If you need to do additional validation on session data, you can do so here.
                session
            }
        }

        //Configure Authentication with login data
        form(AuthName.FORM) {
            userParamName = FormFields.USERNAME
            passwordParamName = FormFields.PASSWORD
            /*
            challenge {
                logger.info ("que coñooo")
                throw AuthenticationException("no se que ha pasado")
            }

             */
            validate { cred: UserPasswordCredential ->
                logger.info ("name -"+cred.name+ "  pass "+cred.password)
                AuthProvider.tryAuth(cred.name, cred.password)
            }
        }
    }

    routing {
        install(StatusPages) {
            //Here you can specify responses on exceptions
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }

        route("/login") {
            authenticate(AuthName.FORM) {
                post {
                    //Principal must not be null as we are authenticated
                    val principal = call.principal<UserIdPrincipal>()!!

                    // Set the cookie to make session auth working
                    call.sessions.set(principal)
                    call.respond(HttpStatusCode.OK, "OK")
                }
            }
        }

        route("/user_info") {
            authenticate(AuthName.SESSION) {
                get {
                    //Principal must not be null as we are authenticated
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.respond(HttpStatusCode.OK, principal)
                }
            }
        }

    }
}

object AuthProvider {

    const val TEST_USER_NAME = "user"
    const val TEST_USER_PASSWORD = "pass"

    fun tryAuth(userName: String, password: String): UserIdPrincipal? {
        return UserIdPrincipal(userName)


        //Here you can use DB or other ways to check user and create a Principal
        if (userName == TEST_USER_NAME && password == TEST_USER_PASSWORD) {
            return UserIdPrincipal(TEST_USER_NAME)
        }

        println("JODEEEEEEEEEEEEERRRRRRRRRRRRRRR")

        return UserIdPrincipal(TEST_USER_NAME)
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
