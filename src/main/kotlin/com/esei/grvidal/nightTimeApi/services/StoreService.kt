package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.serviceInterface.IStoreService
import org.springframework.web.multipart.MultipartFile
import org.springframework.stereotype.Service
import java.nio.file.StandardCopyOption
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

enum class PhotoType{
    BAR_PHOTO,
    USER_PHOTO
}

@Service
class StoreService: IStoreService {

    val uploadPath: String = """C:\Users\reyga\IdeaProjects\NightTimeAPIRest\target\classes"""

    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    override fun store(file: MultipartFile, filename: String, photoType: PhotoType): String {
        logger.info("Starting to store")

        var formattedFilename = filename
            .toLowerCase()
            .replace(" ".toRegex(), "-")
            .replace(":","-")

        if(photoType == PhotoType.USER_PHOTO)
            formattedFilename = formattedFilename.replaceFirst(".","-")


        logger.info("filename = $formattedFilename")
        if (file.isEmpty) {
            logger.error("file is empty")
            throw ServiceException("Failed to store empty file $formattedFilename")
        }
        if (formattedFilename.contains("..")) {
            // This is a security check
            logger.error("file contains .. ")
            throw ServiceException("Cannot store file with relative path outside current directory")
        }

        val storedLocation: String = try {

            val type = if(photoType == PhotoType.BAR_PHOTO) "barpics" else "userpics"
            val location = Paths.get("$uploadPath/$type/$formattedFilename")
            if( !Files.exists(location) ) {
                logger.info("Creating file")
                Files.createFile(location)

            }else logger.error("Already exists")


            Files.copy(
                file.inputStream, location,
                StandardCopyOption.REPLACE_EXISTING
            )
            location.toString()

        } catch (e: IOException) {
            logger.info("Failed to store file $formattedFilename // $e")
            throw ServiceException("Failed to store file $formattedFilename // $e")
        }

        logger.info("StoredLocation =  $storedLocation ")
        return storedLocation.split("classes")[1].replace("\\","/")
    }

    override fun delete(filename: String) {
        val location = Paths.get("$uploadPath/$filename")
        try {
            Files.deleteIfExists(location)
            logger.info("Deleted file $location")

        }catch(e: IOException){
            logger.info("Error deleting file $filename // $e")
            throw ServiceException("Error deleting file $filename // $e")
        }
    }


}