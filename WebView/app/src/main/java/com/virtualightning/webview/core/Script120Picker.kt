package com.virtualightning.webview.core

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/10 00:00:05
 *  Project : WebView
 *  Since Version : Alpha
 */
object Script120Picker: BasePicker<ByteArray>() {
    private var script: ByteArray? = null

    override fun onAction() {
        val curScript = script
        if(curScript == null) {
            val inputStream = CustomApplication.instance.resources.assets.open("120.js")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val readScript = reader.readText().toByteArray()
            this.script = readScript
            inputStream.close()
            completeAction(listOf(readScript))
        }
        else completeAction(listOf(curScript))
    }
}