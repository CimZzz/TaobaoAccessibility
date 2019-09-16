package com.virtualightning.webview.core

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 13:48:05
 *  Project : WebView
 *  Since Version : Alpha
 */
object UserAgentPicker: BasePicker<String>() {
    private var userAgentList: List<String>? = null


    override fun onAction() {
        var curList = userAgentList
        if(curList == null) {
            val inputStream = CustomApplication.instance.resources.assets.open("useragent.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val readJson = reader.readText()
            val obj = JSON.parseArray(readJson)
            curList = obj.filterIsInstance(JSONObject::class.java).mapNotNull { it.getString("ua") }
            userAgentList = curList
            inputStream.close()
        }

        completeAction(curList)
    }
}