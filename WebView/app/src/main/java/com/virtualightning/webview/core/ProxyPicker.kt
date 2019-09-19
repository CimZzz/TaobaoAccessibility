package com.virtualightning.webview.core

import okhttp3.Request
import java.net.InetSocketAddress
import java.net.Proxy

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/18 22:49:15
 *  Project : WebView
 *  Since Version : Alpha
 */
object ProxyPicker: BasePicker<Proxy>() {
    var isDebug = true

    override fun onAction() {
        if(isDebug)
            LogUtils.log("proxy -> [准备] 开始获取代理...")
        HttpUtils.request(
            Request.Builder().url("http://yaochuanlutest.v4.dailiyun.com/query.txt?key=NPDAA70FAB&word=&count=10&rand=false&detail=false"),
            {
                val str = it.body()?.string()?:return@request null
                str.lines().mapNotNull {
                    val hostAndPort = it.split(":")
                    if (hostAndPort.size == 2) {
                        val host = hostAndPort[0]
                        val port =
                            hostAndPort[1].replace("\r", "").replace("\n", "").toIntOrNull() ?: return@request null
                        HttpProxy(host, port)
                    } else null
                }
            }) {
                isSuccess, params ->
            if(isSuccess && params != null && params.isNotEmpty())
                getProxySuccess(params)
            else error("获取代理失败")
        }
    }


    private fun getProxySuccess(proxy: List<HttpProxy>) {
        val proxyList = proxy.mapNotNull {
            try {
                Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(it.host, it.port))
            }
            catch (e: Throwable) {
                null
            }
        }
        if(proxyList.isNullOrEmpty()) {
            error("转化代理失败")
            return
        }

        if(isDebug)
            LogUtils.log("proxy -> [成功] 获取代理成功")
        completeAction(proxyList)
    }

    private fun error(cause: String) {
        if(isDebug)
            LogUtils.log("proxy -> [失败] $cause，将在三秒后重试...")
        SysUtils.registerTimerCallback("config", 3000L, isOnce = true) {
            onAction()
        }
    }



    data class HttpProxy (
        val host: String,
        val port: Int
    ) {
        companion object {
            val Empty = HttpProxy(
                "", 0
            )
        }

        override fun toString(): String {
            return "host -> $host, port -> $port"
        }
    }
}