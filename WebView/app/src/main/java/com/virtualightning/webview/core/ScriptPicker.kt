package com.virtualightning.webview.core

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 11:53:33
 *  Project : WebView
 *  Since Version : Alpha
 */
object ScriptPicker: BasePicker<String>() {
    private var script: String? = null

    override fun onAction() {
        val curScript = script
        if(curScript == null) {
            val inputStream = CustomApplication.instance.resources.assets.open("script2.js")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val readScript = reader.readText()
            this.script = readScript
            inputStream.close()
            completeAction(listOf(readScript))
        }
        else completeAction(listOf(curScript))
    }

}