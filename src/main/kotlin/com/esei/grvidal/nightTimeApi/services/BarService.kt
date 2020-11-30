package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.BarRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class BarService : IBarService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val barRepository: BarRepository? = null


    /**
     * This will list all the bars, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<BarProjection> {

        try {
            return barRepository!!.getBarBy()
            //return barRepository!!.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    @Throws(NotFoundException::class)
    override fun getProjection(idBar: Long): BarProjection {

        try {
            return barRepository!!.getBarById(idBar)

        } catch (e: EmptyResultDataAccessException) {
            throw NotFoundException("No bar with id $idBar have been found")
        }
    }

    /**
     * This will show one bar, if not, will throw a BusinessException or if the object cant be found, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun load(idBar: Long): Bar {
        val op: Optional<Bar>
        try {
            op = barRepository!!.findById(idBar)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idBar")
        }

        return op.get()

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(bar: Bar): Bar {

        try {
            return barRepository!!.save(bar)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(idBar: Long) {
        val op: Optional<Bar>

        try {
            op = barRepository!!.findById(idBar)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $idBar")
        } else {

            try {
                barRepository!!.deleteById(idBar)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }
}


















