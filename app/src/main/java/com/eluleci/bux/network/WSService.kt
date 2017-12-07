package com.eluleci.bux.network

import android.util.Log
import com.eluleci.bux.BuildConfig
import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.source.remote.ProductsDataSourceRemoteApi
import com.eluleci.bux.network.ws.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okio.ByteString

/**
 * Service implementation for web socket. Accepts subscribers, connects
 * on first subscriber, closes connection if all subscribers are unsubscribed.
 */
class WSService : WebSocketListener() {

    private var isConnected = false
    private var ws: WebSocket? = null
    private val subscribers = mutableMapOf<String, WSService.Subscriber>()

    // used to store messages until the connection is successful
    private val pendingMessages = mutableListOf<WebSocketMessage>()

    interface Subscriber {
        fun onMessage(portfolioPerformance: PortfolioPerformance)
        fun onError(throwable: Throwable)
    }

    fun subscribe(id: String, subscriber: WSService.Subscriber) {
        subscribers.put(id, subscriber)
        val message = SubscriptionMessage(listOf(TradingProductIdentifier(id)))
        if (isConnected) {
            // send message directly if already connected
            send(message)
        } else {
            // append message to pending messages to send after successful connection
            pendingMessages.add(message)
            connect()
        }
    }

    fun unsubscribe(id: String) {
        subscribers.remove(id)
        send(SubscriptionMessage(emptyList(), listOf(TradingProductIdentifier(id))))
        if (subscribers.isEmpty()) {
            log("There is no more subscriber left, disconnecting.")
            disconnect()
        }
    }

    private fun notify(message: WSPushMessageWrapper<*>) {
        when (message.t) {
            "connect.connected" -> {
                log("Received connection success message")
                sendPendingMessages()
            }
            "connect.failed" -> {
                log("Received connection failure message")
                notifyError(message.body as BuxNetworkError)
            }
            "portfolio.performance" -> {
                subscribers.values.forEach { subscriber ->
                    subscriber.onMessage(message.body as PortfolioPerformance)
                }
            }
        }
    }

    private fun notifyError(throwable: Throwable) {
        subscribers.values.forEach { subscriber ->
            subscriber.onError(throwable)
        }
    }

    private fun connect() {
        val client = OkHttpClient()
        val request = Request.Builder()
                .header("Authorization", "Bearer ${BuildConfig.JWT_TOKEN}")
                .header("Accept-Language", "nl-NL,en;q=0.8")
                .url(BuildConfig.WS_SERVICE_URL).build()
        ws = client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()
    }

    private fun disconnect() {
        ws?.close(ProductsDataSourceRemoteApi.NORMAL_CLOSURE_STATUS, null)
    }

    private fun send(message: WebSocketMessage) {
        log("Sending message: " + message.toString())
        ws?.send(message.toString())
    }

    private fun sendPendingMessages() {
        pendingMessages.forEach { s -> send(s) }
        pendingMessages.clear()
    }

    override fun onOpen(webSocket: WebSocket, response: Response?) {
        log("Connection is opened.")
        isConnected = true
    }

    override fun onMessage(webSocket: WebSocket?, text: String) {
        log("Received text message: " + text)
        notify(parseMessage(text))
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
        log("Received bytes message: " + bytes.hex())
        notify(parseMessage(bytes.hex()))
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        log("Connection is closed. Code: $code Reason: $reason")
        isConnected = false
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        log("Error: " + t.message)
        isConnected = false
        notifyError(t)
    }

    private fun parseMessage(message: String): WSPushMessageWrapper<*> {
        val wsMessage = Gson().fromJson(message, WSPushMessageWrapper::class.java)
        return when (wsMessage.t) {
            "connect.connected" -> {
                val type = object : TypeToken<WSPushMessageWrapper<ConnectionInfo>>() {}.type
                Gson().fromJson<WSPushMessageWrapper<ConnectionInfo>>(message, type)
            }

            "connect.failed" -> {
                val type = object : TypeToken<WSPushMessageWrapper<BuxNetworkError>>() {}.type
                Gson().fromJson<WSPushMessageWrapper<BuxNetworkError>>(message, type)
            }

            else -> {
                val type = object : TypeToken<WSPushMessageWrapper<PortfolioPerformance>>() {}.type
                Gson().fromJson<WSPushMessageWrapper<PortfolioPerformance>>(message, type)
            }
        }
    }

    private fun log(message: String) {
        Log.d("WSService", message)
    }
}