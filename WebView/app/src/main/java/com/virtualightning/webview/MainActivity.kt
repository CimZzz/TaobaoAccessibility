package com.virtualightning.webview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        webView.addJavascriptInterface(JSObject(), "tskcj")
        webView.loadUrl("https://ai.m.taobao.com/index.html?pid=mm_229720104_254050230_70587600194")
        btn.setOnClickListener {
            webView.loadUrl("javascript:tskcj.getValue(String())")
//            webView.loadUrl("javascript:tskcj.dialogLocationY(String(document.getElementById(\"nc_1-stage-1\").getBoundingClientRect().top))")
//            webView.loadUrl("javascript:tskcj.dialogLocationX(String(document.getElementById(\"nc_1-stage-1\").getBoundingClientRect().left))")
//            webView.loadUrl("javascript:tskcj.dialogLocation(" +
//                    "String(document.getElementById(\"J_MMREDBOX_DIALOG_CONTENT\").getBoundingClientRect().y + document.getElementById(\"nc_1-stage-1\").getBoundingClientRect().y) + \",\" + String(document.getElementById(\"J_MMREDBOX_DIALOG_CONTENT\").getBoundingClientRect().x + document.getElementById(\"nc_1-stage-1\").getBoundingClientRect().x))")
//            webView.loadUrl("javascript:tskcj.clientWidth(String(document.documentElement.scrollWidth))")
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    inner class JSObject {
        var x: Float? = null
        var y: Float? = null
        var width: Float? = null

        @JavascriptInterface
        fun getValue(value: String?) {
            Log.v("CimZzz", "value -> $value")
        }

        @JavascriptInterface
        fun dialogLocationX(pos: String?) {
            Log.v("CimZzz", "posX -> $pos")
            x = pos?.toFloatOrNull()
            check()
        }

        @JavascriptInterface
        fun dialogLocationY(pos: String?) {
            Log.v("CimZzz", "posY -> $pos")
            y = pos?.toFloatOrNull()
            check()
        }
        @JavascriptInterface
        fun clientWidth(pos: String?) {
            Log.v("CimZzz", "width -> $pos")
            width = pos?.toFloatOrNull()
            check()
        }

        private fun check() {
            val x = x?:return
            val y = y?:return
            val width = width?:return
            val webViewWidth = webView.width.toFloat()
            val ratio = webViewWidth / width
            Log.v("CimZzz", "Convert x -> ${x * ratio}, y -> ${y * ratio}")
        }
    }
}
