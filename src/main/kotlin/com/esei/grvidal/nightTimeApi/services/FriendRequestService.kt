package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.FriendRequestInsertDTO
import com.esei.grvidal.nightTimeApi.dto.toFriendRequest
import com.esei.grvidal.nightTimeApi.repository.FriendRequestRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.FriendRequest
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class FriendRequestService : IFriendRequestService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var friendRequestRepository: FriendRequestRepository

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * This will list all the chats, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<FriendRequest> {

        try {
            return friendRequestRepository.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    @Throws(ServiceException::class)
    override fun listByUserAnswer(userId: Long): List<FriendRequest> {
        try {
            return friendRequestRepository.findFriendRequestByUserAnswer_Id(userId)

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    /**
     * This will show one Chat, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun load(friendRequestId: Long): FriendRequest {
        val op: Optional<FriendRequest>
        try {
            op = friendRequestRepository.findById(friendRequestId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro la solicitud con el id $friendRequestId")
        }

        return op.get()

    }

    /**
     * This will save a new [Friends], if not, will throw an Exception
     */
    override fun save(friendRequest: FriendRequestInsertDTO): Long {


        //Check if the relation already exists
        var op = friendRequestRepository.findFriendRequestByUserAsk_IdAndUserAnswer_Id(
            friendRequest.idUserAsk, friendRequest.idUserAnswer
        )
        if (!op.isPresent) {

            //check if the opposite relation already exists
            op = friendRequestRepository.findFriendRequestByUserAsk_IdAndUserAnswer_Id(
                friendRequest.idUserAnswer, friendRequest.idUserAsk
            )
            if (!op.isPresent) {

                //Check if the userAsk exists
                val userAsk = userRepository.findById(friendRequest.idUserAsk)
                    .orElseThrow { NotFoundException("User who asks with id ${friendRequest.idUserAsk} not found") }

                //Check if the userAnswer exists
                val userAnswer = userRepository.findById(friendRequest.idUserAnswer)
                    .orElseThrow { NotFoundException("User who answers with id ${friendRequest.idUserAnswer} not found") }


                return friendRequestRepository.save(
                    friendRequest.toFriendRequest(userAsk, userAnswer)
                ).id
            }
        }
        throw AlreadyExistsException("Relation already exists")

    }

    /**
     * This will remove a Request through its id, if not, will throw an Exception,
     * or if it cant find it, it will throw a NotFoundException
     *
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(friendRequestId: Long) {
        val op: Optional<FriendRequest>

        try {
            op = friendRequestRepository!!.findById(friendRequestId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro la solicitud con el id $friendRequestId")
        } else {

            try {
                friendRequestRepository!!.deleteById(friendRequestId)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }

}


















