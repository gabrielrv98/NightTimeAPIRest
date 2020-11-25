package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dao.BarRepository
import com.esei.grvidal.nightTimeApi.dao.CityRepository
import com.esei.grvidal.nightTimeApi.dao.EventRepository
import com.esei.grvidal.nightTimeApi.dao.UserRepository
import com.esei.grvidal.nightTimeApi.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDate

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



        val user1 = User("Gabriel Rguez","grvidal","Hey there i'm using NightTime", LocalDate.now(),cityOu)
        val user2 = User("Nuria Sotelo","pinkxnut",".", LocalDate.of(2020,10,30) ?: null,cityOu)
        val user3 = User("Irene Monterey","monteRey","Pues soy mas de playa", LocalDate.of(2020,10,30) ?: null,cityVigo)
        val user4 = User("Jose Negro","joseju","Todo lo que se pueda decir es irrelevante")

        userRepositories!!.save(user1)
        userRepositories!!.save(user2)
        userRepositories!!.save(user3)
        userRepositories!!.save(user4)

        if(user2.friends == null){
            user2.friends = listOf(user1)
        }else user2.friends?.plus(user1)

        if(user1.friends == null){
            user1.friends = listOf(user2)
        }else user1.friends?.plus(user2)

        user2.friends =  user2.friends?.plus(user3)

        userRepositories!!.save(user1)
        userRepositories!!.save(user2)




    }
}



fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}
