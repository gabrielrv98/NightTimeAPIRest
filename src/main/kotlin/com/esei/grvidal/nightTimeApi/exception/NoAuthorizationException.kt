package com.esei.grvidal.nightTimeApi.exception

/**
 * Exception shown when the user Id doesn't match with the owner of the information
 */
class NoAuthorizationException(message: String?): Exception(message) {

}