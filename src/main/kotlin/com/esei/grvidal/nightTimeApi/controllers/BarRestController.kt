package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.dto.BarDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toBar
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.ICityService
import com.esei.grvidal.nightTimeApi.serviceInterface.IPhotoService
import com.esei.grvidal.nightTimeApi.serviceInterface.IStoreService
import com.esei.grvidal.nightTimeApi.services.PhotoType
import com.esei.grvidal.nightTimeApi.utils.Constants
import com.esei.grvidal.nightTimeApi.utils.Constants.Companion.ERROR_HEADER_TAG
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

/**
 * This is Bar Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_BAR)
class BarRestController {

    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    //Bar Service with the logic of bar
    @Autowired
    private lateinit var barService: IBarService

    //City Service with the logic of photosURL
    @Autowired
    private lateinit var photoService: IPhotoService

    //City Service with the logic of city
    @Autowired
    private lateinit var cityService: ICityService

    //City Service with the logic of storing images
    @Autowired
    private lateinit var storeService: IStoreService


    /**
     * Listen to a Get with an id from a city and shows all Bars in that city
     *
     * @param cityId Id from the city to show
     * @return all Bars in that city with id [cityId]
     */
    @GetMapping("/byCity/{idCity}")
    fun listByCity(
        @PathVariable("idCity") cityId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<BarProjection>> {

        return ResponseEntity(barService.listByCity(cityId, page, size), HttpStatus.OK)

    }

    /**
     * Listen to a get with the id of a bar to return the information of that bar that is not on the preview
     *
     * @return [BarDetailsProjection] of the bar with the matching [idBar]
     * @param idBar id of the bar
     */
    @GetMapping("/{id}/details")
    fun getDetails(@PathVariable("id") idBar: Long): ResponseEntity<BarDetailsProjection?> {

        return try {
            ResponseEntity(barService.getDetails(idBar), HttpStatus.OK)
        } catch (e: NotFoundException) {
            val responseHeader = HttpHeaders()
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(
                null,
                responseHeader,
                HttpStatus.NOT_FOUND
            )
        }
    }


    @GetMapping(
        value = ["/{idBar}/photo/{idPhoto}"],
        produces = [MediaType.IMAGE_JPEG_VALUE]
    )
    fun getPicture(
        @PathVariable("idBar") idBar: Long,
        @PathVariable("idPhoto") idPhoto: Int
    ): ResponseEntity<ByteArray?> {
        val responseHeader = HttpHeaders()


        val photoProjection = photoService.getPhotoDir(idBar, idPhoto)

        logger.info("photoProjection = ${photoProjection?.getId()} ${photoProjection?.getDir()}")
        return if (photoProjection != null) {

            responseHeader.set("id_photo", photoProjection.getId().toString())

            return try {
                val photo: InputStream = javaClass
                    .getResourceAsStream(photoProjection.getDir())

                ResponseEntity(photo.readBytes(), responseHeader, HttpStatus.OK)

            } catch (e: NullPointerException) {
                responseHeader.set(ERROR_HEADER_TAG, e.message)
                ResponseEntity(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
            }

        } else {
            responseHeader.set(ERROR_HEADER_TAG, "no photo with id $idPhoto")
            logger.info("photoProjection = it was null")
            ResponseEntity(null,responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{idBar}/photo")
    fun addPicture(
        @PathVariable("idBar") idBar: Long,
        @RequestParam img: MultipartFile
    ): ResponseEntity<Boolean> {


        val responseHeader = HttpHeaders()
        return try {

            logger.info("filename original ${img.originalFilename}")

            //Get the new name of the img
            val idNewPhoto = barService.getDetails(idBar).getPhotos()

            // save new img
            val newName = storeService.store(
                img,
                "bar${idBar}_$idNewPhoto.jpg",
                PhotoType.BAR_PHOTO
            )

            logger.info("New file name $newName, bar $idBar")

           photoService.addPhotoDir(barService.load(idBar), newName)

            ResponseEntity(true, HttpStatus.OK)


        } catch (e: NotFoundException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: ServiceException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }


    /**
     * Listen to a Post and a requestBody with a [BarDTOInsert] to create a new Bar
     *
     * @param bar new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody bar: BarDTOInsert): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        return try {
            val barId = barService.save(bar.toBar(cityService.load(bar.cityId)))
            responseHeader.set("location", Constants.URL_BASE_BAR + "/" + barId)
            ResponseEntity(true,responseHeader, HttpStatus.CREATED)

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Patch and a requestBody with a [BarDTOEdit] to update a Bar
     * if any attribute is null, the original will be used, and if any schedule is text "null", will be set as null
     *
     * @param idBar id of the bar that will be updated
     * @param barEdit bar with the attributes to modify
     *
     * If there is any mistake in Schedule object it will just omit the pair key value
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idBar: Long, @RequestBody barEdit: BarDTOEdit): ResponseEntity<Boolean> {

        return try {
            barService.update(idBar, barEdit)
            ResponseEntity(true,HttpStatus.OK)

        } catch (e: NotFoundException) {
            val responseHeader = HttpHeaders()
            responseHeader.set(ERROR_HEADER_TAG,e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)
        }


    }

    /**
     * Listen to a Delete and a Id to delete a Bar
     *
     * @param idBar id of the deleted bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idBar: Long): ResponseEntity<Boolean> {
        return try {

            barService.remove(idBar)
            ResponseEntity(true,HttpStatus.NO_CONTENT)

        } catch (e: NotFoundException) {
            val responseHeader = HttpHeaders()
            responseHeader.set(ERROR_HEADER_TAG,e.message)
            ResponseEntity(false,responseHeader, HttpStatus.NOT_FOUND)
        }
    }
}