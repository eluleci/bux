package com.eluleci.bux.network

/**
 * Used for error handling.
 */
data class BuxNetworkError(
        override var message: String,
        val errorCode: String,
        val developerMessage: String
) : RuntimeException() {
    override fun toString(): String {
        return "$errorCode: $message"
    }
}