package com.virtualightning.webview.core

import com.alibaba.fastjson.JSON
import okhttp3.Request

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 11:53:33
 *  Project : WebView
 *  Since Version : Alpha
 */
object PhoneNumberPicker: BasePicker<String>() {
    var isDebug = true


    override fun onAction() {
        requestPhoneNumber()
    }


    private fun requestPhoneNumber() {
        if(isDebug)
            LogUtils.log("phone -> [准备] 开始获取手机号...")
        HttpUtils.request(
            Request.Builder().url("https://www.oeebee.com/phone?count=30"),
            parser = {
                JSON.parseObject(it.body()?.string())
            },
            callback = {
                isSuccess, obj ->
                if(isSuccess && obj != null) {
                    val phoneArr = obj.getJSONArray("phones")?.filterIsInstance(String::class.java)
                    if(phoneArr != null && phoneArr.isNotEmpty()) {
                        getPhoneSuccess(phoneArr)
                        return@request
                    }
                }

                error("请求失败")
                return@request
            }
        )
    }

    private fun getPhoneSuccess(phoneList: List<String>) {
        if(isDebug)
            LogUtils.log("phone -> [成功] 获取手机号成功: $phoneList")
        completeAction(phoneList)
    }

    private fun error(cause: String) {
        if(isDebug)
            LogUtils.log("phone -> [失败] $cause，将在三秒后重试...")
        SysUtils.registerTimerCallback("phone", 3000L, isOnce = true) {
            requestPhoneNumber()
        }
    }
}