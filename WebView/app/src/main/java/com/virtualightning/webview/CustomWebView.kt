package com.virtualightning.webview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/04 22:41:11
 *  Project : WebView
 *  Since Version : Alpha
 */
class CustomWebView: WebView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    var o: JSONArray? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val item = JSONObject()

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                o = JSONArray()
                item["action"] = "down"
            }
            MotionEvent.ACTION_MOVE -> {
                item["action"] = "move"
            }
        }

        item["x"] = event.x
        item["y"] = event.y
        item["time"] = event.eventTime

        if(event.action == MotionEvent.ACTION_UP) {
            item["action"] = "up"
            o?.add(item)
            Log.v("CimZzz", o.toString())
        }
        else o?.add(item)
        return super.onTouchEvent(event)
    }
}