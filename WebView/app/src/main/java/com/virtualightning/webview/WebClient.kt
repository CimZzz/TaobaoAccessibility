package com.virtualightning.webview

import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.virtualightning.webview.core.LogUtils
import okhttp3.*
import java.io.ByteArrayInputStream
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.ref.SoftReference
import java.nio.charset.Charset
import java.util.concurrent.ConcurrentHashMap

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/19 00:54:57
 *  Project : WebView
 *  Since Version : Alpha
 */
class WebClient:
    WebViewClient() {
    companion object {
        private val cacheMap = ConcurrentHashMap<String, SoftReference<CacheObject>>()
    }

//    private val ref = WeakReference(webView)
    private val MARKER = "AJAXINTERCEPT"
    private val bodyMap = ConcurrentHashMap<String, String>()
    var script: String? = null
    var httpClient: OkHttpClient? = null

    fun receiverBody(id: String, body: String) {
        bodyMap[id] = body
    }

    fun clearMap() {
        bodyMap.clear()
    }


    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val url = request?.url?.toString()
        if(url != null &&
            (url.startsWith("https://lego.alicdn.com/mm/lego2") ||
                    url.startsWith("https://g.alicdn.com/thx/cube/1.1.0/cube-min.css") ||
                    url.startsWith("http://lego.alicdn.com/mm/lego2") ||
                    url.startsWith("http://g.alicdn.com/thx/cube/1.1.0/cube-min.css"))
        ) {
        }
        else if(url != null)
            try {
                val cookie = CookieManager.getInstance().getCookie(url)
                val builder = Request.Builder()
                    .headers(Headers.of(request.requestHeaders))

                if(cookie != null)
                    builder.addHeader("Cookie", cookie)

                if(request.method.toUpperCase() == "GET") {
                    var cacheObject: CacheObject? = null
//                    if(url.startsWith("https://af.alicdn.com/AWSC/uab/120.js") || url.startsWith("https://g.alicdn.com/AWSC/WebUMID/1.76.1/um.js"))
//                        cacheObject = cacheMap[url]?.get()
//
//                    if(cacheObject != null) {
//                        return WebResourceResponse(cacheObject.contentType,
//                            cacheObject.encoding,
//                            200,
//                            "OK",
//                            cacheObject.headers,
//                            ByteArrayInputStream(cacheObject.body)
//                        )
//                    }
//                    LogUtils.log("get url: $url")

                    builder.url(url)
                        .get()
                    val response = httpClient?.newCall(builder.build())?.execute()?:throw RuntimeException("失败")
                    var content = response.body()?.bytes()
                    response.close()
                    var contentType = response.header("content-type")
                    if(contentType != null) {
                        val typeArr = contentType.split(";")
                        val type = typeArr[0]
                        if(type == "text/html" && content != null) {
                            contentType = type
                            /// 修改
                            val contentStr = String(content)
                            val idx = contentStr.indexOf("<head>")
                            if(idx != -1) {
                                val beginIdx = idx + 6
                                content = (contentStr.substring(0, beginIdx) + script + contentStr.substring(beginIdx)).toByteArray()
                            }
                        }
                    }

                    val encoding = response.header("content-encoding")?: Charset.defaultCharset().displayName()
                    val headers = response.headers().toMultimap().let {
                        val hashMap = HashMap<String, String>()
                        it.forEach { entry ->
                            val key = entry.key
                            val value = entry.value
                            hashMap[key] = value.joinToString(";")
                        }
                        hashMap
                    }


                    if(url.startsWith("https://af.alicdn.com/AWSC/uab/120.js") || url.startsWith("https://g.alicdn.com/AWSC/WebUMID/1.76.1/um.js")) {
                        val newCacheObject = CacheObject()
                        newCacheObject.lastUrl = url
                        newCacheObject.body = content
                        newCacheObject.contentType = contentType
                        newCacheObject.encoding = encoding
                        newCacheObject.headers = headers
                        cacheMap[url] = SoftReference(newCacheObject)
                    }


                    return WebResourceResponse(contentType,
                        encoding,
                        response.code(),
                        "OK",
                        headers,
                        ByteArrayInputStream(content)
                    )
                }
                else {
                    val urlArr = url.split(MARKER)
                    if(urlArr.size != 2) {
//                        LogUtils.log("failed url: $url")
                        throw RuntimeException("失败")
                    }

                    val realUrl = urlArr[0]
                    val bodyId = urlArr[1]

                    val body = bodyMap[bodyId]
                    bodyMap.remove(bodyId)
                    if(body == null) {
//                        LogUtils.log("failed url: $url")
                        throw RuntimeException("失败")
                    }
//                    LogUtils.log("post url: $url")

                    builder.url(realUrl)
                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), body.toByteArray()))
                    val response = httpClient?.newCall(builder.build())?.execute()?: throw RuntimeException("失败")
                    val content = response.body()?.bytes()
                    LogUtils.log("cookie: ${cookie}")
                    LogUtils.log("headers: ${response.request().headers().toMultimap()}")
                    LogUtils.log("body: $body")
                    LogUtils.log("content: ${if(content != null) String(content) else "null"}")
                    response.close()
                    var contentType = response.header("content-type")

                    if(contentType != null) {
                        val typeArr = contentType.split(";")
                        contentType = typeArr[0]
                    }

                    return WebResourceResponse(contentType,
                        response.header("content-encoding")?: Charset.defaultCharset().displayName(),
                        response.code(),
                        "OK",
                        response.headers().toMultimap().let {
                            val hashMap = HashMap<String, String>()
                            it.forEach { entry ->
                                val key = entry.key
                                val value = entry.value
                                hashMap[key] = value.joinToString(" ; ")
                            }
                            hashMap
                        },
                        ByteArrayInputStream(content)
                    )
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        return WebResourceResponse("", "", ByteArrayInputStream(ByteArray(0)))
    }
}