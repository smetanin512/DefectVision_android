package com.example.hakatonapplication.socket

import com.google.gson.Gson

abstract class BaseSocket( val gson: Gson) {
    var listener: SocketCallback? = null

    abstract fun setupSocket()

    abstract fun setupAddress()

    abstract fun close()

    abstract fun sendMessage()

    abstract fun connectAndListen()

    abstract fun reconnect()
}

interface SocketCallback {
    fun connected()

    fun receivedMessage(responseMessage: ResponseMessage)
}

enum class SocketType {

    WEB_SOCKET,

    IO;
}

object SocketFactory {

    fun createSocket(gson: Gson): BaseSocket = IOSocket(gson)
}
