package com.esei.grvidal.nightTimeApi.exception

//must return 401 Unauthorized

class AuthenticationException(message: String?): Exception(message) {

}


/*
@ControllerAdvice
class CustomExceptionsHandler {

    @ExceptionHandler(AuthenticationException::class)
    fun authenticationExceptionHandler(e: Exception): ResponseEntity<String> {
        return ResponseEntity("authToken missing or not valid!", HttpStatus.UNAUTHORIZED)
    }

}
 */