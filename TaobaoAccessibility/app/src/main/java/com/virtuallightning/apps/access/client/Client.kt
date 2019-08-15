package com.virtuallightning.apps.access.client

import com.virtuallightning.apps.access.core.SubscribeService

class Client(private val service: SubscribeService) {
    companion object {
        private const val OUTER_STATUS_DEFAULT = 0
        private const val OUTER_STATUS_PREPARING = 1
        private const val OUTER_STATUS_PICKING = 2

        private const val INNER_STATUS_DISCONNECTED = 0
        private const val INNER_STATUS_CONNECTING = 1
        private const val INNER_STATUS_CONNECTED = 2
    }

    private var outStatus = OUTER_STATUS_DEFAULT
    private var innerStatus = INNER_STATUS_DISCONNECTED


    fun firePreparing() {
        outStatus =
            OUTER_STATUS_PREPARING
        if(!checkConnected()) {
            if(innerStatus != INNER_STATUS_CONNECTING) {
                innerStatus =
                    INNER_STATUS_CONNECTING
            }

            return
        }
    }

    private fun checkConnected(): Boolean = innerStatus == INNER_STATUS_CONNECTED

    fun dispose() {

    }
}