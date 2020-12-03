package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.FriendRequestRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import com.esei.grvidal.nightTimeApi.model.FriendRequest
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendRequestBusiness
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class FriendRequestBusiness : IFriendRequestBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val friendRequestRepository: FriendRequestRepository? = null

    /**
     * This will list all the chats, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<FriendRequest> {

        try {
            return friendRequestRepository!!.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    @Throws(ServiceException::class)
    override fun listByUserAnswer(userId: Long): List<FriendRequest> {
        try {
            return friendRequestRepository!!.findFriendRequestByUserAnswer_Id(userId)

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
            op = friendRequestRepository!!.findById(friendRequestId)
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
    @Throws(ServiceException::class, AlreadyExistsException::class)
    override fun save(friendRequest: FriendRequest): FriendRequest {
        try {

            var op = friendRequestRepository!!.findFriendRequestByUserAsk_IdAndUserAnswer_Id(
                    friendRequest.userAsk.id, friendRequest.userAnswer.id)
            if (!op.isPresent) {
                op = friendRequestRepository!!.findFriendRequestByUserAsk_IdAndUserAnswer_Id(
                        friendRequest.userAnswer.id, friendRequest.userAsk.id)
                if (!op.isPresent) {
                    friendRequest.answer = AnswerOptions.NOT_ANSWERED
                    return friendRequestRepository!!.save(friendRequest)
                }
            }
            throw AlreadyExistsException("Relation already exists")


        } catch (e: AlreadyExistsException) {
            throw AlreadyExistsException(e.message)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
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


















