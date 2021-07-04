package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.builders.ChatViewBuilder
import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipDTOUpdate
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.*
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.repository.FriendshipRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendshipService
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

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
    private lateinit var friendshipRepository: FriendshipRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * Lists all the users that where the friendship answer is 1 (YES) ( they are friends)
     */
    override fun listFriendsSnapByUser(userId: Long, page: Int, size: Int): List<FriendshipSnapView> {
        return friendshipRepository
            .findFriendshipsFromUser(userId, PageRequest.of(page, size))
            .map {
                FriendshipSnapView(it, userId)
            }
    }

    /**
     * Lists all friendships with Messages from an User
     */
    override fun listChatByUser(userId: Long): List<ChatView> {
        return friendshipRepository.getChatsWithMessages(userId)
            .map { ChatViewBuilder.createChatView(it, userId, true) }
            .sortedByDescending { it.messages[0].date }
            .sortedByDescending { it.messages[0].time }
    }


    /**
     * This will show one Friendship,
     * if the object cant be found, it will throw a NotFoundException
     *
     *
     */
    override fun loadById(friendsId: Long): FriendshipProjection {

        return friendshipRepository.findFriendshipById(friendsId)
            .orElseThrow { NotFoundException("Couldn't find relationship with id $friendsId") }
    }


    /**
     * This will show one Friendship,
     * if the object cant be found, it will throw a NotFoundException
     *
     */
    override fun loadByUsers(user1Id: Long, user2Id: Long): FriendshipProjection {

        return friendshipRepository.findFriendshipByUsersIds(user1Id, user2Id)
            .orElseThrow { NotFoundException("Couldn't find relationship between users $user1Id and $user2Id") }
    }


    /**
     * This will save a new [Friendship], if not, will throw an Exception
     */
    override fun save(userAsk: Long, userAnswer: Long): Long {

        //Check if the relation already exists
        val friends = friendshipRepository.findFriendshipByUsersIds(userAsk, userAnswer)

        if (!friends.isPresent) {

            //Check if the userAnswer exists ( if service has been called, userAsk has to be logged)
            if (!userRepository.existsById(userAnswer))
                throw  NotFoundException("User requested with id $userAnswer not found")

            return friendshipRepository.save(
                Friendship(User(userAsk), User(userAnswer))
            ).id
        }
        throw AlreadyExistsException("Relationship already exists")
    }

    override fun update(userId: Long, friendshipParam: FriendshipDTOUpdate) {

        val friendshipRequestDB = friendshipRepository.findById(friendshipParam.id)
            .orElseThrow { NotFoundException("Couldn't find relationship with id ${friendshipParam.id}") }

        //only non accepted requests can be updated
        if (friendshipRequestDB.answer != AnswerOptions.NOT_ANSWERED) {
            throw ServiceException("Friendship already accepted, can only be deleted")
        }

        //Only the userAnswer can update the request
        if (userId != friendshipRequestDB.userAnswer.id) {
            throw ServiceException("Error: Only userAnswer can update the request")
        } else {
            if (friendshipParam.answer == AnswerOptions.YES) {

                //Save accepted request
                friendshipRequestDB.answer = friendshipParam.answer
                friendshipRepository.save(friendshipRequestDB)

            } else {
                //remove request
                this.remove(friendshipRequestDB.id)
            }

        }

    }

    /**
     * This will remove a friendship through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    override fun remove(friendsId: Long) {
        friendshipRepository.deleteById(friendsId)

    }

    override fun getFriendsRequest(idUser: Long): List<UserFriendView> {
        return friendshipRepository.getFriendshipsRequest(idUser).map { UserFriendView(it, idUser) }
    }

    override fun getCountFriendsRequest(idUser: Long): Int {
        return friendshipRepository.getCountAllByUserAnswerAndAnswer(idUser)
    }

    override fun friendshipState(idUser1: Long, idUser2: Long): AnswerOptions {
        val friendshipList = friendshipRepository.findFriendsByUserAsk_IdOrUserAnswer_Id(idUser1, idUser1)
            .filter { it.userAnswer.id == idUser2 || it.userAsk.id == idUser2 }

        return if (friendshipList.isEmpty()) AnswerOptions.NO
        else friendshipList[0].answer
    }

    override fun getCountFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): Int {

        return friendshipRepository.getCountFriendsFromUserOnDate(
            idUser = idUser,
            idCity = dateCityDTO.nextCityId,
            date = dateCityDTO.nextDate
        )

    }

    override fun getFriendsOnDate(
        idUser: Long,
        dateCityDTO: DateCityDTO,
        page: Int,
        size: Int
    ): List<UserSnapView> {

        val friendList = friendshipRepository.getFriendsOnDate(

            idUser = idUser,
            idCity = dateCityDTO.nextCityId,
            date = dateCityDTO.nextDate,
            pageable = PageRequest.of(page, size)
        )
        return friendList.map { UserSnapView(it, idUser) }
    }


    override fun listFriendshipsIds(idUser: Long): List<Long> {
        return friendshipRepository.getFriendshipIds(idUser)
    }

    override fun getCountFriends(userId: Long): Int {
        return friendshipRepository.getCountFriends(userId)
    }

}