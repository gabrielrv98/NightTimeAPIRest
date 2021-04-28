package com.esei.grvidal.nightTimeApi.projections


/**
 * Interface to show a list of chats
 */
class UserFriendView(
    var friendshipId: Long,
    var userId: Long,
    var userNickname: String,
    var state: String,
    var image: Boolean
) : java.io.Serializable {


    /**
     *  Main constructor that receives a Friendship and an ID from the user who made the request,
     *  extracts the other user and calls the private constructor
     */
    constructor(
        friendship: FriendshipProjection,
        userId: Long
    ) : this(
        friendshipId = friendship.getId(),
        user = if (friendship.getUserAsk().getId() == userId) friendship.getUserAnswer()
        else friendship.getUserAsk()
    )

    /**
     *  Private constructor, receives the friendship ID, the set of messages an the oth
     *
     */
    private constructor(
        friendshipId: Long,
        user: UserProjection
    ) : this(
        friendshipId = friendshipId,
        userId = user.getId(),
        userNickname = user.getNickname(),
        state = user.getState(),
        image = user.getPicture() != null
    )


}
