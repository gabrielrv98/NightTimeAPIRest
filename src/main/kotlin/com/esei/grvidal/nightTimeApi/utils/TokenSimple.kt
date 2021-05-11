package com.esei.grvidal.nightTimeApi.utils
/**
 *  Class with a Singleton object that manages the token for authentication
 */

import com.esei.grvidal.nightTimeApi.exception.NotLoggedException
import kotlin.jvm.Throws

class TokenSimple {
    //HashMap with the tokens for authorization
    object TokenSimple : HashMap<Long,String>(hashMapOf()){

        /**
         * Receives [id] of the user that is also the key for the hashMap, [token] as the secure string
         *
         * @return true if the parameter token equals the saved token
         * @exception [NotLoggedException] if the token for the [id] is null
         */
        @Throws(NotLoggedException::class)
        fun securityCheck(id: Long, token: String): Boolean {
            TokenSimple[id]?.let {
                return it == token
            }

            throw NotLoggedException("user with id $id is not logged in")
        }

        /**
         * Receives [length] as an int for the length of the generated string
         *
         * @return a random string of length [length] and characters from the array [charset]
         */
        fun tokenGen(length: Int): String {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789$@-_=//\\*$&" // 71 characters
            var token: String

            do {
                token = (1..length)
                    .map { charset.random() }
                    .joinToString("")

            } while (TokenSimple.values.contains(token))//If token is repeated, a new one is calculated

            return token
        }
    }


}