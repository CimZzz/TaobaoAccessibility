package com.virtualightning.webview.core

import android.os.SystemClock
import android.webkit.CookieManager
import okhttp3.Request
import java.util.*

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 19:53:19
 *  Project : WebView
 *  Since Version : Alpha
 */
object ConfigRegistry {
    private const val MAX_PROXY_TIME = 1000L

    private val callbackList =  LinkedList<RegistryCallback>()
    private var currentWaitCount = 0
    private var isDoing = false
    private var currentProxyTime: Long = 0L
    private var currentProxy: HttpProxy = ConfigRegistry.HttpProxy.Empty

    var isDebug = true

    fun register(callback: RegistryCallback) {
        callbackList.add(callback)
    }

    fun waitConfig() {
        currentWaitCount ++
        checkConfig()
    }

    fun stopConfig() {
        currentWaitCount --
    }

    private fun checkConfig() {
        if(currentWaitCount == callbackList.size) {
            if(!isDoing) {
                if(isDebug)
                    LogUtils.log("config -> [准备] 开始配置中...")
                isDoing = true
                /// 先重置代理
                ProxyUtils.revertProxyKKPlus()
                /// 清空缓存
                val cookieManager = CookieManager.getInstance()
                cookieManager.removeSessionCookies(null)
                cookieManager.removeAllCookie()
                cookieManager.flush()
                try {
//                    CustomApplication.instance.cacheDir.parentFile.delete()
                }
                catch (e: Throwable) {
                    if(isDebug)
                        error("清理缓存文件失败")
                    return
                }

                val currentTime = SystemClock.elapsedRealtime()
                val diffTime = currentTime - currentProxyTime
                if(currentProxy != HttpProxy.Empty && diffTime < MAX_PROXY_TIME) {
                    if(isDebug)
                        LogUtils.log("config -> [成功] 未达到最大代理时间")
                    getProxySuccess(currentProxy, false)
                    return
                }

                /// 获取代理
                HttpUtils.request(
                    Request.Builder().url("http://yaochuanlutest.v4.dailiyun.com/query.txt?key=NPDAA70FAB&word=&count=1&rand=false&detail=false"),
                    {
                        val str = it.body()?.string()?:return@request null
                        val hostAndPort = str.split(":")
                        if(hostAndPort.size == 2) {
                            val host = hostAndPort[0]
                            val port = hostAndPort[1].replace("\r", "").replace("\n", "").toIntOrNull()?:return@request null
                            return@request HttpProxy(host, port)
                        }
                        else return@request null
                    }) {
                    isSuccess, params ->
                    if(isSuccess && params != null) {
                        SysUtils.runOnMain {
                            getProxySuccess(params, true)
                        }
                    }
                    else SysUtils.runOnMain {
                        if(isDebug)
                            error("获取代理失败")
                    }
                }
            }
        }
    }

    private fun getProxySuccess(proxy: HttpProxy, needConfig: Boolean) {
        if(needConfig && proxy != HttpProxy.Empty)
            ProxyUtils.setProxyKKPlus(proxy.host, proxy.port)
        this.currentProxy = proxy
        this.currentProxyTime = SystemClock.elapsedRealtime()
        isDoing = false
        if(isDebug)
            LogUtils.log("config -> [成功] 配置成功，代理 $proxy")
        if(currentWaitCount == callbackList.size) {
            if(isDebug)
                LogUtils.log("config -> [成功] 正在触发回调")
            currentWaitCount = 0
            callbackList.forEach { it() }
        }
    }

    private fun error(cause: String) {
        if(isDebug)
            LogUtils.log("config -> [失败] $cause，将在三秒后重试...")
        SysUtils.registerTimerCallback("config", 3000L, isOnce = true) {
            isDoing = false
            checkConfig()
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