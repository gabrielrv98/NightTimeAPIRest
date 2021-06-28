package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.PhotoURL
import com.esei.grvidal.nightTimeApi.projections.PhotoProjection
import com.esei.grvidal.nightTimeApi.repository.PhotoRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IPhotoService
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


    override fun addPhotoDir(bar: Bar, dir: String) {
        photoRepository.save(
            PhotoURL(
                dir =  dir,
                bar = bar)
        )
    }

    override fun getPhotoDir(barId: Long, nPicture: Int): PhotoProjection? {

         return photoRepository.findByBar_Id(barId, PageRequest.of(nPicture, 1)).getOrElse(0) { null }
    }
}