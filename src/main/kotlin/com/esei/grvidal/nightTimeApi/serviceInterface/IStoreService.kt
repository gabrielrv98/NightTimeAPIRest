package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.services.PhotoType
import org.springframework.web.multipart.MultipartFile
import kotlin.jvm.Throws

interface IStoreService {


    @Throws(ServiceException::class)
    fun store(file: MultipartFile, filename: String, photoType: PhotoType) : String

    @Throws(ServiceException::class)
    fun delete(filename: String)
}