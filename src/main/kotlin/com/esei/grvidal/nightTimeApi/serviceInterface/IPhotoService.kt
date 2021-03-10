package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.projections.PhotoProjection

interface IPhotoService {
    fun getPhotoDir(barId:Long, nPicture: Int): PhotoProjection?
}