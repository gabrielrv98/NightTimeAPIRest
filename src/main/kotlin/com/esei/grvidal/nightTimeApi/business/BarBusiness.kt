package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.dao.BarRepository
import com.esei.grvidal.nightTimeApi.dao.EventRepository
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class BarBusiness : IBarBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val barRepository: BarRepository? = null



    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(BusinessException::class)
    override fun list(): List<Bar> {

        try {
            return barRepository!!.findAll()
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }


    /**
     * This will show one bar, if not, will throw a BusinessException or if the object cant be found, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun load(idBar: Long): Bar {
        val op: Optional<Bar>
        try {
            op = barRepository!!.findById(idBar)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idBar")
        }

        return op.get()

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(BusinessException::class)
    override fun save(bar: Bar): Bar {

        try {
            return barRepository!!.save(bar)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun remove(idBar: Long) {
        val op: Optional<Bar>

        try {
            op = barRepository!!.findById(idBar)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idBar")
        } else {

            try {
                barRepository!!.deleteById(idBar)
            } catch (e: Exception) {
                throw BusinessException(e.message)
            }
        }

    }
}


















