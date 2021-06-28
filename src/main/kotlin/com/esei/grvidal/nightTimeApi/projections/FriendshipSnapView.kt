package com.esei.grvidal.nightTimeApi.projections

/**
 * Interface to show a list of chats
 */
class FriendshipSnapView(
    val userId: Long,
    val friendshipId :Long,
    var username: String,
    val name: String,
    val image: Boolean
) : java.io.Serializable {


    /**
     *  Main constructor that receives a Friendship and an ID from the user who made the request,
     *  extracts the other user and calls the private constructor
     */
    constructor(
        friendship: FriendshipProjection,
        userId: Long
    ) : this(
        user = if (friendship.getUserAsk().getId() == userId) friendship.getUserAnswer()
        else friendship.getUserAsk(),
        friendshipId = friendship.getId()
    )

    /**
     *  Constructor, receives the friendship ID, the set of messages an the oth
     *
     */
    constructor(
        user: UserProjection,
        friendshipId: Long
    ) : this(
        userId = user.getId(),
        friendshipId = friendshipId,
        username = user.getNickname(),
        name = user.getName(),
        image = user.getPicture() != null

    )


}