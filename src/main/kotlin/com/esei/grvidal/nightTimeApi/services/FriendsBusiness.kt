package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.FriendsRepository
import com.esei.grvidal.nightTimeApi.dao.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class FriendsBusiness : IFriendsBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val friendsRepository: FriendsRepository? = null

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val messageRepository: MessageRepository? = null

    /**
     * This will list all the chats, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<Friends> {

        try {
            return friendsRepository!!.findAll()
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * Lists all the friendships of one User
     */
    override fun listFriendsByUser(userId: Long): List<FriendProjection> {


        friendsRepository?.let {
            return it.findFriendsByUser1_IdOrUser2_IdAndAnswer(userId, userId, AnswerOptions.YES)
        }
        return listOf()

    }

    /**
     * Lists all the friendships of one User
     */
    override fun listUserByUser(userId: Long): List<UserProjection> {

        var list: List<UserProjection> = listOf()

        friendsRepository?.let {
            it.findFriendsByUser1_IdOrUser2_IdAndAnswer(userId, userId, AnswerOptions.YES).forEach {friendProjection ->
                if (friendProjection.getUser1().getId() == userId) list = list.plus(friendProjection.getUser2())
                else if (friendProjection.getUser2().getId() == userId) list = list.plus(friendProjection.getUser1())
            }

        }
        return list

    }

    /**
     * Lists all the friendships of one User
     */
    @Throws(NotFoundException::class, ServiceException::class)
    override fun listChatsByUser(userId: Long): List<Friends> {


        friendsRepository?.let {
            return it.findFriendsByUser1_IdOrUser2_Id(userId, userId)

        }
        return listOf()

    }

    /**
     * Lists all the chats of one User
     */
    @Throws(NotFoundException::class, ServiceException::class)
    override fun listMessagesFromChat(friendsId: Long): List<Message> {
        try {

            return messageRepository!!.findAllByFriends_Id(friendsId)

        } catch (e: NotFoundException) {
            throw NotFoundException(e.message)

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    /**
     * This will show one Chat, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun load(friendsId: Long): Friends {
        val op: Optional<Friends>
        try {
            op = friendsRepository!!.findById(friendsId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $friendsId")
        }

        return op.get()

    }

    /**
     * This will save a new [Friends], if not, will throw an Exception
     */
    @Throws(ServiceException::class, AlreadyExistsException::class)
    override fun save(friends: Friends): Friends {
        try {

            val list = friendsRepository!!.findFriendsByUser1_IdOrUser2_Id(friends.user1.id, friends.user1.id)
            list.forEach {
                if (it.user1.id == friends.user2.id || it.user2.id == friends.user2.id)
                    throw AlreadyExistsException("Friendship already exists")
            }
            return friendsRepository!!.save(friends)


        } catch (e: AlreadyExistsException) {
            throw AlreadyExistsException(e.message)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    /**
     * This will remove a CHAT through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     * //TODO It shouldn't be removed
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(friendsId: Long) {
        val op: Optional<Friends>

        try {
            op = friendsRepository!!.findById(friendsId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al chat con el id $friendsId")
        } else {

            try {
                friendsRepository!!.deleteById(friendsId)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }

    /**
     * This will save a new Message, if not, will throw an Exception
     * Checks if the signed user is in the friends relationship
     */
    @Throws(ServiceException::class)
    override fun saveMsg(msg: Message): Message {
        try {
            val friends = msg.friends

            if (friends.user1 != msg.user && friends.user2 != msg.user)
                throw ServiceException("User it's not on the friendship")
            else {
                return messageRepository!!.save(msg)
            }

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }
}


















