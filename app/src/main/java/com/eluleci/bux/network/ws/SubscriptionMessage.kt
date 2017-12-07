package com.eluleci.bux.network.ws

import com.google.gson.annotations.SerializedName

interface WebSocketMessage

data class SubscriptionMessage(
        val subscribeTo: List<TradingProductIdentifier> = emptyList(),
        val unsubscribeFrom: List<TradingProductIdentifier> = emptyList()
) : WebSocketMessage

data class TradingProductIdentifier(
        val id: String
) {
    override fun toString(): String {
        return "trading.product.$id"
    }
}

data class WSPushMessageWrapper<T>(
        @SerializedName("t")
        val t: String,

        @SerializedName("id")
        val id: String,

        @SerializedName("v")
        val v: String,

        @SerializedName("body")
        val body: T
)

data class ConnectionInfo(
        @SerializedName("sessionInfo")
        val sessionInfo: String
)
