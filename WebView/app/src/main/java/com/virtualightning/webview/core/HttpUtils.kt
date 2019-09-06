package com.virtualightning.webview.core

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 08:22:23
 *  Project : WebView
 *  Since Version : Alpha
 */
object HttpUtils {
    private val client = OkHttpClient.Builder()
        .connectTimeout(4000L, TimeUnit.MILLISECONDS)
        .readTimeout(4000L, TimeUnit.MILLISECONDS)
        .build()

    fun execute(builder: Request.Builder): Response? = client.newCall(builder.build()).execute()

    fun<T> request(builder: Request.Builder, parser: RequestParser<T>, callback: RequestCallback<T>): CallContainer<T> {
        val call = client.newCall(builder.build())
        val container = CallContainer<T>(call, callback)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                container.doCallback(false, null)
                call.cancel()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    container.doCallback(true, parser(response))
                }
                catch (e: Throwable) {
                    container.doCallback(false, null)
                }
                call.cancel()
            }
        })
        return container
    }

    class CallContainer<T>(private val call: Call, private val callback: RequestCallback<T>) {
        private var isDestroy = false
        fun destroy() {
            try {
                call.cancel()
            }
            catch (e: Throwable) {

            }
            isDestroy = true
        }

        internal fun doCallback(isSuccess: Boolean, data: T?) {
            SysUtils.runOnMain {
                if(!isDestroy)
                    callback(isSuccess, data)
            }
        }
    }
}