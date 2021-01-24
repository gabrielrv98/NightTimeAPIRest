package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipInsertDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipUpdateDTO
import com.esei.grvidal.nightTimeApi.repository.FriendshipRepository
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import com.esei.grvidal.nightTimeApi.projections.UserFriendView
import com.esei.grvidal.nightTimeApi.projections.UserSnapProjection
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendshipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/**
 * Friendship.kt service, is the implementation of the DAO interface
 *
 */
@Service
class FriendshipService : IFriendshipService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var friendshipRepository: FriendshipRepository

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * Lists all the users that where the friendship answer is 1 (YES) ( they are friends)
     */
    override fun listUsersFromFriendsByUser(userId: Long): List<UserFriendView> {
        return friendshipRepository.findFriendshipsFromUser(userId).map { UserFriendView(it, userId) }
    }

    /**
     * Lists all friendships with Messages from an User
     */
    override fun listUsersWithChatFromFriendsByUser(userId: Long): List<ChatView> {
        return friendshipRepository.getChatsWithMessages(userId).map { ChatView(it, userId) }
    }


    /**
     * This will show one Chat, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     *
     *
     */
    override fun load(friendsId: Long): FriendshipProjection {

        return friendshipRepository.findFriendshipById(friendsId)
            .orElseThrow { NotFoundException("Couldn't find relationship with id $friendsId") }
    }


    /**
     * This will save a new [Friendship], if not, will throw an Exception
     */
    override fun save(friendship: FriendshipInsertDTO): Long {

        //Check if the userAnswer exists
        val userAnswer = userRepository.findByNickname(friendship.userAnswer)
            .orElseThrow { NotFoundException("No user with nickname ${friendship.userAnswer} were found") }

        //Check if the relation already exists
        var op = friendshipRepository.findFriendshipByUserAsk_IdAndUserAnswer_Id(
            friendship.idUserAsk, userAnswer.id
        )
        if (!op.isPresent) {

            //check if the opposite relation already exists
            op = friendshipRepository.findFriendshipByUserAsk_IdAndUserAnswer_Id(
                userAnswer.id, friendship.idUserAsk
            )
            if (!op.isPresent) {

                //Check if the userAsk exists
                val userAsk = userRepository.findById(friendship.idUserAsk)
                    .orElseThrow { NotFoundException("User who asks with id ${friendship.idUserAsk} not found") }

                return friendshipRepository.save(
                    Friendship(userAsk, userAnswer)
                ).id
            }
        }
        throw AlreadyExistsException("Relationship already exists")
    }

    override fun update(friendshipParam: FriendshipUpdateDTO) {
        val friendshipDB = friendshipRepository.findById(friendshipParam.id)
            .orElseThrow { NotFoundException("No friendship with id ${friendshipParam.id} were found") }

        friendshipDB.answer = friendshipParam.answer
        friendshipRepository.save(friendshipDB)
    }

    /**
     * This will remove a CHAT through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     * //TODO Should be marked as "deleted" instead of being deleted
     */
    override fun remove(friendsId: Long) {
        friendshipRepository.deleteById(friendsId)

    }

    /**
     * This will save a new Message, if not, will throw an Exception
     * Checks if the signed user is in the friendship relationship
     */
    @Throws(ServiceException::class)
    override fun saveMsg(msg: Message): Message {
        val friends = msg.friendship

        if (friends.userAsk != msg.user && friends.userAnswer != msg.user)
            throw ServiceException("User it's not on the friendship")
        else {
            return messageRepository.save(msg)
        }
    }

    override fun getFriendsRequest(idUser: Long): List<UserFriendView> {
        return friendshipRepository.getFriendshipsRequest(idUser).map{ UserFriendView(it,idUser) }
    }

    override fun getCountFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): Int {

        var numberFriends = friendshipRepository.getCountFriendsFromUserAskOnDate(idUser,dateCityDTO.nextCityId,dateCityDTO.nextDate)
        numberFriends += friendshipRepository.getCountFriendsFromAnswerOnDate(idUser,dateCityDTO.nextCityId,dateCityDTO.nextDate)
        return numberFriends
    }

    override fun getFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): List<UserSnapProjection> {

        val friendList =
            friendshipRepository.getFriendsFromUserAskOnDate(idUser,dateCityDTO.nextCityId,dateCityDTO.nextDate).toMutableList()
        friendList += friendshipRepository.getFriendsFromUserAnswerOnDate(idUser,dateCityDTO.nextCityId,dateCityDTO.nextDate)

        return friendList.map{ UserSnapProjection(it,idUser) }

    }

}


















