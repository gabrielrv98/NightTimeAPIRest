package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dao.BarRepository
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Schedule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.esei.grvidal.nightTimeApi.dao")
@EntityScan("com.esei.grvidal.nightTimeApi.model")
class NightTimeApiApplication : CommandLineRunner {

    @Autowired
    val barRepository: BarRepository? = null

    //@Autowired
    //val eventRepositories: EventRepository? = null

    override fun run(vararg args: String?) {

        val bar1 = Bar("Luxus", "Algun pijo", "Mercedes 2º")

        val schedule = Schedule(monday = true, tuesday = false, wednesday = false,
                thursday = true, friday = false, saturday = true, sunday = true)
        bar1.schedule = schedule

        barRepository!!.save(bar1)

        val bar2 = Bar("Patio andaluz", "Aida", "Calle turbia")
        bar2.schedule = Schedule(monday = true, tuesday = false, wednesday = false,
                thursday = true, friday = false, saturday = true, sunday = true)
        barRepository!!.save(bar2)
/*

        val event1 = Event("copas baratas", "copas a menos de 1 euro", bar = bar1)
        val event2 = Event("Fardos gratis", "Fardos gratis", bar = bar1)
        val event3 = Event("Algo habra", "No lo tenemos muy claro", bar = bar2)


 */
        //eventRepositories!!.save(event1)


    }
}

fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}
