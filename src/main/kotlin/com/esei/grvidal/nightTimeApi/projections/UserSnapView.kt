package com.esei.grvidal.nightTimeApi.projections


/**
 * Interface to show a list of chats
 */
class UserSnapView(
    var userId: Long,
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
        else friendship.getUserAsk()
    )

    /**
     *  Constructor, receives the friendship ID, the set of messages an the oth
     *
     */
    constructor(
        user: UserProjection
    ) : this(
        userId = user.getId(),
        username = user.getNickname(),
        name = user.getName(),
        image = user.getPicture() != null
    )


}