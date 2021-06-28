package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.PhotoProjection

interface IPhotoService {
    fun addPhotoDir(bar: Bar, dir: String)
    fun getPhotoDir(barId:Long, nPicture: Int): PhotoProjection?
}