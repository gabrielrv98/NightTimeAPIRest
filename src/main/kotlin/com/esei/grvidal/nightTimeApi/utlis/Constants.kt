package com.esei.grvidal.nightTimeApi.utlis

/**
 * Class with the constants of the api, used to minimize errors
 */
class Constants {

    companion object {
        private const val URL_API_BASE = "/api"
        private const val URL_API_VERSION = "/v1"
        private const val URL_BASE = URL_API_BASE + URL_API_VERSION

        //BaseAPI endpoint for bars
        const val URL_BASE_BAR = "$URL_BASE/bar"
        const val URL_BASE_EVENT = "$URL_BASE/event"
        const val URL_BASE_USER = "$URL_BASE/user"

        const val ERROR_HEADER_TAG = "error"
    }
}