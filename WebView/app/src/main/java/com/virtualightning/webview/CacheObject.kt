package com.virtualightning.webview

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/19 02:40:28
 *  Project : WebView
 *  Since Version : Alpha
 */
class CacheObject {
    var lastUrl: String? = null
    var body: ByteArray? = null
    var headers: Map<String, String>? = null
    var contentType: String? = null
    var encoding: String? = null
}