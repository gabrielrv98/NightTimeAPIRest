package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.projections.PhotoProjection
import com.esei.grvidal.nightTimeApi.repository.PhotoRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IPhotoService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service


/**
 * Photo service, the implementation of the DAO interface
 *
 */
@Service
class PhotoService: IPhotoService {

    @Autowired
    lateinit var photoRepository: PhotoRepository

    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    override fun getPhotoDir(barId: Long, nPicture: Int): PhotoProjection? {

        val a = photoRepository.findByBar_Id(barId, PageRequest.of(nPicture, 1))
        logger.info("fingBy = ${a.size}")
        return if( a.isEmpty())  null
        else  a[0]


        //return photoRepository.findByBar_Id(barId, PageRequest.of(nPicture, 1)).getOrElse(0) { null }
    }
}