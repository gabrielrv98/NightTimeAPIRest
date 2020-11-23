package com.esei.grvidal.nightTimeApi

import com.esei.grvidal.nightTimeApi.dao.BarRepository
import com.esei.grvidal.nightTimeApi.model.Bar
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

    override fun run(vararg args: String?) {

        val bar1 = Bar("Luxus", "Algun pijo", "Mercedes 2º")
        barRepository!!.save(bar1)

        //val bar2 = Bar("Patio andaluz", "Aida", "Calle turbia")
        //barRepository!!.save(bar2)
    }
}

fun main(args: Array<String>) {
    runApplication<NightTimeApiApplication>(*args)
}
