package com.example.hakatonapplication.socket

import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import timber.log.Timber


class IOSocket(gson: Gson) : BaseSocket() {
    private var mSocket: Socket? = null
    private var address: String = Constants.DEFAULT_ADDRESS
    private var path: String = Constants.PATH

    override fun setupSocket() {
        try {
            mSocket = IO.socket(address, createOptions())
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d(Constants.LOG_ERROR, "Failed to connect")
        }
    }

    override fun setupAddress() {
        this.address = Constants.DEFAULT_ADDRESS
        this.path = Constants.PATH
    }

    override fun connectAndListen() {

        try {
            mSocket?.on(Socket.EVENT_CONNECT, onConnect)
            mSocket?.on(Socket.EVENT_DISCONNECT) { println(Constants.LOG_DISCONNECTED) }
            mSocket?.connect()

            mSocket?.on(Constants.SERVER_RESPONSE, onResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d(Constants.LOG_ERROR, "Failed to connect")
        }

    }

    private var onConnect = Emitter.Listener {
        if (mSocket?.connected() == true) {
            listener?.connected()
        }
    }

    private var onResponse = Emitter.Listener {
        print(it)
        try {
            val string = (it.first() as JSONObject).toString()
            val response = gson.fromJson(string, ResponseMessage::class.java)
            listener?.receivedMessage(response)
        } catch (e: java.lang.Exception) {
            Timber.d(Constants.LOG_ERROR, e.stackTrace.toString())
        }
    }

    private fun createOptions(): IO.Options {

        return IO.Options.builder()
            .setPath(path)
            .setForceNew(true)
            .setTimeout(Constants.TIMEOUT)
            .setReconnectionAttempts(Constants.RECONNECTION_ATTEMPTS)
            .build()
    }

    override fun sendMessage( ) {
        mSocket?.emit(Constants.USER_REQUEST)
    }


    override fun close() {
        this.unsubscribe()
    }

    private fun unsubscribe() {
        mSocket?.disconnect()
    }

    override fun reconnect() {
        close()
        connectAndListen()
    }

    object Constants {
        const val LOG_ERROR = "ERROR"

        const val LOG_DISCONNECTED = "disconnected"

        const val SERVER_RESPONSE = "server_response"

        const val USER_REQUEST = "user_request"

        const val DEFAULT_ADDRESS = "http://94.26.229.85:5000"

        const val PATH = "/socket.io"

        const val TIMEOUT: Long = 2000

        const val RECONNECTION_ATTEMPTS = 3
    }
}
