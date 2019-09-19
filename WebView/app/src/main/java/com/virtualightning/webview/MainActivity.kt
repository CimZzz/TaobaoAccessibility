package com.virtualightning.webview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.virtualightning.webview.core.PhoneNumberPicker
import com.virtualightning.webview.core.ProxyPicker
import com.virtualightning.webview.core.StatisticUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main3.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)
        ProxyPicker.isDebug = false
        PhoneNumberPicker.isDebug = false
//        btn.setOnClickListener {
//            OkHttpClient.Builder().proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("117.84.72.127", 57114)))
//                .build().newCall(Request.Builder().url("https://www.oeebee.com/phone").build()).enqueue(object: Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        LogUtils.log(e)
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        LogUtils.log(response.body()?.string())
//                    }
//                })
//        }
//        val s = WebView(this)
//        btn.setOnClickListener {
//            ScriptPicker.register(200) {
//                webView.loadUrl("javascript:$it")
//                webView.loadUrl("javascript:window.CimZzz.xc(\"1\", \"true\")")
//                SysUtils.runOnMainDelay(2000L) {
//                    webView.loadUrl("javascript:window.CimZzz.ii(\"true\", \"13808070931\")")
//                    SysUtils.runOnMainDelay(2000L) {
//                        webView.loadUrl("javascript:window.CimZzz.mcl()")
//                    }
//                }
//                }
//        }
//        input.setOnClickListener {
//            ScriptPicker.register(200) {
////                LogUtils.log("script: $it")
//                webView.loadUrl("javascript:$it")
//                webView.loadUrl("javascript:window.CimZzz.i(\"15800394958\")")
//            }
//        }
//        webView.clearCache(true)
//        webView.clearHistory()
//        cacheDir.parentFile.delete()
//        ProxyUtils.setProxy(webView, "36.25.43.160", 57114, Application::class.java.name)
        webView.start()
//        webView.loadUrl("https://ai.m.taobao.com/index.html?pid=mm_229720104_254050230_70587600194")
//        webView1.viewId = 1
//        webView2.viewId = 2
//        webView3.viewId = 3
//        webView4.viewId = 4
//        webView5.viewId = 5
//        webView6.viewId = 6
//        webView7.viewId = 7
//        webView8.viewId = 8
//        webView9.viewId = 9jujklj8uju
//        webView1.isDebug = false
//        webView2.isDebug = false
//        webView3.isDebug = false
//        webView4.isDebug = false
//        webView5.isDebug = false
//        webView6.isDebug = false
//        webView7.isDebug = false
//        webView8.isDebug = false
//        webView9.isDebug = false
//        webView1.start()
//        webView2.start()
//        webView3.start()
//        webView4.start()
//        webView5.start()
//        webView6.start()
//        webView7.start()
//        webView8.start()
//        webView9.start()
    }

//    private fun setProxyHostField(proxyServer: HttpHost): Boolean {
//        // Getting network
//        var networkClass: Class<*>? = null
//        var network: Any? = null
//        try {
//            networkClass = Class.forName("android.webkit.Network")
//            val networkField = networkClass!!.getDeclaredField("sNetwork")
//            network = getFieldValueSafely(networkField, null)
//        } catch (ex: Exception) {
//            Log.e(ProxyManager::class.java!!.getName(), "error getting network")
//            return false
//        }
//
//        if (network == null) {
//            Log.e(ProxyManager::class.java!!.getName(), "error getting network : null")
//            return false
//        }
//        var requestQueue: Any? = null
//        try {
//            val requestQueueField = networkClass
//                .getDeclaredField("mRequestQueue")
//            requestQueue = getFieldValueSafely(requestQueueField, network)
//        } catch (ex: Exception) {
//            Log.e(ProxyManager::class.java!!.getName(), "error getting field value")
//            return false
//        }
//
//        if (requestQueue == null) {
//            Log.e(ProxyManager::class.java!!.getName(), "Request queue is null")
//            return false
//        }
//        var proxyHostField: Field? = null
//        try {
//            val requestQueueClass = Class.forName("android.net.http.RequestQueue")
//            proxyHostField = requestQueueClass
//                .getDeclaredField("mProxyHost")
//        } catch (ex: Exception) {
//            Log.e(ProxyManager::class.java!!.getName(), "error getting proxy host field")
//            return false
//        }
//
//        synchronized(synchronizer) {
//            val temp = proxyHostField!!.isAccessible()
//            try {
//                proxyHostField!!.setAccessible(true)
//                proxyHostField!!.set(requestQueue, proxyServer)
//            } catch (ex: Exception) {
//                Log.e(ProxyManager::class.java!!.getName(), "error setting proxy host")
//            } finally {
//                proxyHostField!!.setAccessible(temp)
//            }
//        }
//        return true
//    }
//
//    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
//    private fun getFieldValueSafely(field: Field, classInstance: Any?): Any {
//        val oldAccessibleValue = field.isAccessible()
//        field.setAccessible(true)
//        val result = field.get(classInstance)
//        field.setAccessible(oldAccessibleValue)
//        return result
//    }
}
