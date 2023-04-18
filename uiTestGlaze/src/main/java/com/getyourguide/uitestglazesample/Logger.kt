package com.getyourguide.uitestglazesample

class Logger(private val logger: (String) -> Unit) {

    fun i(message: String) {
        logger.invoke(message)
    }
}
