package com.esei.grvidal.nightTimeApi.utlis

/**
 * Enum with the option to answer a friendship request
 * TODO add a 4th state "ERASED", where users can't send messages on the chat but they can read the already written chat
 */
enum class AnswerOptions {
    NOT_ANSWERED,
    YES,
    NO
}