package com.example.hakatonapplication.socket

class ResponseMessage(
    val data: ResponseData?
)

data class ResponseData(val first: Int?, val second: Int?, val third: Int?, val fourth: Int?)