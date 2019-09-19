package com.virtualightning.webview

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.virtualightning.webview.core.*
import okhttp3.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.Proxy
import java.net.URLEncoder

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 00:07:31
 *  Project : WebView
 *  Since Version : Alpha
 */
class NewCustomWebView: WebView {
    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)



    init {
        client = WebClient()
        val settings = settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = false
        settings.blockNetworkImage = true
        settings.mixedContentMode = 0
//        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webViewClient = client
        addJavascriptInterface(JSObject(this), "tskcj")
    }

    companion object {
        const val targetUrl = "https://ai.m.taobao.com/index.html?pid=mm_229720104_254050230_70587600194"

        private const val State_Init = 0
        private const val State_WaitScript = 1
        private const val State_WaitPhoneNum = 2
        private const val State_WaitProxy = 3
        private const val State_WaitUserAgent = 4
        private const val State_WaitPage = 5
        private const val State_WaitPage_Transfer_Proxy = 6
        private const val State_WaitDialog = 7
        private const val State_WaitInput = 8
        private const val State_WaitInput_Done = 9
        private const val State_WaitScroll = 10
        private const val State_WaitScroll_Click = 11
        private const val State_WaitValid = 12

        /// 重试次数
        private const val Error_Page = 0
        private const val Error_Number = 2


        /// 最多等待 10 秒
        private const val Wait_Page = 20000L
        private const val Wait_Page_Transfer_Proxy = 3000L
        private const val Wait_Dialog = 4000L
        private const val Wait_Input = 1000L
        private const val Wait_Input_Done = 1000L
        private const val Wait_Scroll = 4000L
        private const val Wait_Scroll_Click = 1000L
        private const val Wait_Valid = 4000L
    }

    private var state: Int = 0
    private var stateTime: Long = 0L
    private var script: String = ""
    private var script120: ByteArray? = null
    private var scriptUM: ByteArray? = null
    private var phoneNum: String = ""
    private var phaseName: String = ""
    private var userAgent: String = ""
    private var numberFailedCount = 0
    private var httpClient: OkHttpClient = OkHttpClient()
    private val client: WebClient

    private var isPageSuccess: Boolean = false
    private var errorCount = 0
    private var callbackCode: Int = 0

    private var isHasBound: Boolean = true
    private var isScrollOver: Boolean = false

    private var pickerSet = HashSet<BasePicker<*>>()
    var isDebug: Boolean = false
    var viewId: Int = 0

    private fun changePhase(phase: String) {
        this.phaseName = phase
        logPhase("进入阶段")
    }

    private fun logPhase(any: Any?) {
        Log.v("CimZzz", "webView[$viewId] -> [${phaseName}阶段] ${any.toString()}")
    }

    private fun<T> registerPicker(picker: BasePicker<T>, callback: PickerCallback<T>) {
        if(pickerSet.contains(picker))
            return

        pickerSet.add(picker)
        picker.register(viewId, callback)
    }

    private fun unregisterPicker(picker: BasePicker<*>) {
        picker.unregister(viewId)
        pickerSet.remove(picker)
    }

    private fun increaseCode() {
        callbackCode ++
        if(callbackCode >= 10000)
            callbackCode = 0
    }

    fun stop() {
        SysUtils.unregisterTimerCallback("timer$viewId")
        pickerSet.forEach {
            it.unregister(viewId)
        }
        pickerSet.clear()
        state = State_Init
        phoneNum = ""
        userAgent = ""
        isPageSuccess = false
        errorCount = 0
        increaseCode()
        isScrollOver = false
        isHasBound = false
        httpClient.dispatcher()?.cancelAll()
        if(isDebug)
            changePhase("初始化")
    }

    fun start() {
        if(state == State_Init) {
            errorCount = 0
            waitScript()
        }
    }

    /// 阶段-1 获取脚本
    private fun waitScript() {
        state = State_WaitScript
        if(isDebug)
            changePhase("脚本")
        if(script.isNotEmpty())
            receiverScript(script)
        else registerPicker(ScriptPicker, ::receiverScript)
    }

    private fun receiverScript(script: String) {
        this.script = script
        if(isDebug)
            logPhase("已获取注入脚本")
        client.script = this.script
        unregisterPicker(ScriptPicker)
        registerPicker(Script120Picker, ::receiverScript120)
    }

    private fun receiverScript120(script: ByteArray) {
        this.script120 = script
        if(isDebug)
            logPhase("已获取120脚本")
        unregisterPicker(Script120Picker)
        registerPicker(ScriptUMPicker, ::receiverScriptUM)
    }

    private fun receiverScriptUM(script: ByteArray) {
        this.scriptUM = script
        if(isDebug)
            logPhase("已获取UM脚本")
        unregisterPicker(ScriptUMPicker)
        waitNumber()
    }

    /// 阶段-2 获取手机号码
    private fun waitNumber() {
        state = State_WaitPhoneNum
        numberFailedCount = 0
        resetPageResource(true)
        if(isDebug)
            changePhase("号码")
        registerPicker(PhoneNumberPicker, ::receiverNum)
    }

    private fun receiverNum(num: String) {
        phoneNum = num
        if(isDebug)
            logPhase("收到号码: $num")
        unregisterPicker(PhoneNumberPicker)
        StatisticUtils.newPhone()
        waitProxy()
    }


    /// 阶段-3 等待代理
    private fun waitProxy() {
        state = State_WaitProxy
        resetPageResource(true)
        stopLoading()
        if(isDebug)
            changePhase("代理")
        registerPicker(ProxyPicker, ::receiverHttpProxy)
//        ConfigRegistry.waitConfig()
    }

    private fun receiverHttpProxy(proxy: Proxy) {
        if(isDebug)
            logPhase("收到代理: $proxy")
        unregisterPicker(ProxyPicker)
        httpClient = OkHttpClient.Builder()
//            .proxy(proxy)
            .build()
        waitUserAgent()
    }

    /// 阶段-4 获取 UA
    private fun waitUserAgent() {
        state = State_WaitUserAgent
        if(isDebug)
            changePhase("UserAgent")
        registerPicker(UserAgentPicker, ::receiverUserAgent)
    }

    private fun receiverUserAgent(userAgent: String) {
        this.userAgent = userAgent
        if(isDebug)
            logPhase("收到UA: $userAgent")
        unregisterPicker(UserAgentPicker)
        waitPage()
    }


    /// 阶段-5 开始加载网页
    /// 同时开始打开计时器
    private fun waitPage() {
        if(state != State_WaitPage) {
            state = State_WaitPage
            if(isDebug)
                changePhase("加载页面")
        }
        resetPageResource()

        SysUtils.registerTimerCallback("timer$viewId", 200L, isOnce = false) {
            checkStateTimer()
        }

        client.httpClient = this.httpClient
        loadUrl(targetUrl)
    }

    /// 回收等待页面时启用的资源
    private fun resetPageResource(resetErrorCode: Boolean = false) {
        if(resetErrorCode)
            errorCount = 0
        SysUtils.unregisterTimerCallback("timer$viewId")
        increaseCode()
        resetStateTime()
        client.clearMap()
        isPageSuccess = false
        isHasBound = false
        isScrollOver = false

    }

    /// 页面加载失败
    /// 后续弹窗，输入失败均调用此方法重试
    private fun waitPageError() {
        /// 如果验证阶段号码失败次数超过最大次数，则忽略该号码
        if(numberFailedCount > Error_Number) {
            if(isDebug)
                logPhase("验证失败次数超过 $Error_Number 次数，忽略号码 $phoneNum")
            StatisticUtils.phoneError()
            waitNumber()
            return
        }

        StatisticUtils.error()
        errorCount ++
        if(errorCount > Error_Page) {
            increaseCode()
            state = State_WaitPage_Transfer_Proxy
            errorCount = 0
            if(isDebug)
                logPhase("页面超过 $Error_Page 次重试失败，将在 ${Wait_Page_Transfer_Proxy / 1000} 秒后重新获取代理")
        }
        else {
            if(isDebug)
                logPhase("第 $errorCount 次失败，正在重试...")
            waitPage()
        }
    }

    private fun pageLoadSuccess() {
        if(state != State_WaitPage)
            return
        isPageSuccess = true
        if(isDebug)
            logPhase("已加载页面")
        checkPageSuccess()
    }

    /// 判断页面是否加载成功
    private fun checkPageSuccess() {
        if(!isPageSuccess || state != State_WaitPage)
            return

        if(isDebug)
            logPhase("页面已就绪")
        waitDialog()
    }

    /// 阶段-5 开始显示弹窗
    /// 弹窗是通过点击红包按钮或者自动显示的
    private fun waitDialog() {
        state = State_WaitDialog
        if(isDebug)
            changePhase("弹窗")
        increaseCode()
        resetStateTime()
        exec("xc", "\"true\"")
    }

    private fun dialogShowed() {
        if(state != State_WaitDialog)
            return

        if(isDebug)
            logPhase("成功显示弹窗")
        waitInput()
    }

    /// 阶段-6 输入号码
    private fun waitInput() {
        state = State_WaitInput
        if(isDebug)
            changePhase("输入")
        increaseCode()
        resetStateTime()
        /// 先等待一秒
    }

    private fun waitInputDone() {
        state = State_WaitInput_Done
        increaseCode()
        resetStateTime()
        exec("i", "\"true\",\"$phoneNum\"", code = null)
    }

    private fun inputSuccess() {
        if(isDebug)
            logPhase("输入完成")
        waitScroll()
    }

    /// 阶段-7 滑动滑块
    /// 滑块大约有 8 秒的等待时间，当第一次获取 bound 时开始滑动
    /// 当布局发生变动时，重新开始滑动并且重置时间
    private fun waitScroll() {
        state = State_WaitScroll
        if(isDebug)
            changePhase("滑块")
        increaseCode()
        resetStateTime()
    }

    private fun receiverBound(
        contentWidth: Float,
        orgViewLeft: Float,
        orgViewRight: Float,
        orgViewTop: Float,
        orgViewBottom: Float
    ) {
        if (isHasBound)
            return

        if(contentWidth == 0f)
            return

        isHasBound = true

        val ratio = width / contentWidth

        val touchX = (orgViewLeft + (orgViewRight - orgViewLeft) / 2) * ratio
        val touchY = (orgViewTop + (orgViewBottom - orgViewTop) / 2) * ratio
        if(isDebug)
            logPhase("发现弹窗位置点击: $touchX, $touchY")
        mockTouch(this).down(touchX, touchY)
            .up(distanceTime = 20L)
        waitScrollClick()
    }

    /// 阶段-9 等待滑动点击
    private fun waitScrollClick() {
        state = State_WaitScroll_Click
        if(isDebug)
            changePhase("滑动点击")
        increaseCode()
        resetStateTime()
    }

    private fun receiverClickWaitTime() {
        if(isDebug)
            logPhase("完成点击，执行滑块动作")
        exec("mcl")
        waitValid()
    }

    /// 阶段-10 等待验证
    private fun waitValid() {
        state = State_WaitValid
        if(isDebug)
            changePhase("验证")
        increaseCode()
        resetStateTime()
    }

    private fun receiverValid() {
        val number = phoneNum
        val isDebug = isDebug
        if(isDebug)
            logPhase("验证成功，$number 已成功领取奖励")
        StatisticUtils.success()
        try {
            httpClient.newCall(Request.Builder()
                .url("http://q.mmhots.com:7351/cl/u/recv.php?w=${URLEncoder.encode(number, "utf-8")}&p=mi-test-three-web-view&t=${SysUtils.getElapsedSecond()}")
                .build()).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                }
            })
        }
        catch (e: Throwable) {

        }
        waitNumber()
    }

    private fun receiverComplete() {
        if(isDebug)
            logPhase("$phoneNum 达到最大领取次数，忽略")
        StatisticUtils.success()
        waitNumber()
    }

    /// 业务超时设置
    private fun resetStateTime() {
        stateTime = SystemClock.elapsedRealtime()
    }

    private fun checkOver(overTime: Long): Boolean {
        val currentTime = SystemClock.elapsedRealtime()
        val diffTime = currentTime - stateTime
        return diffTime >= overTime
    }

    private fun checkStateTimer() {
        when(state) {
            State_WaitPage -> {
                if(checkOver(Wait_Page)) {
                    /// 等待页面超时
                    waitPageError()
                }
                else exec("z")
            }
            State_WaitPage_Transfer_Proxy -> {
                if(checkOver((Wait_Page_Transfer_Proxy))) {
                    /// 重新开始配置
                    waitProxy()
                }
            }
            State_WaitDialog -> {
                if(checkOver(Wait_Dialog)) {
                    waitPageError()
                }
                else exec("xc", "\"false\"")
            }
            State_WaitInput -> {
                if(checkOver(Wait_Input)) {
                    waitInputDone()
                }
            }
            State_WaitInput_Done -> {
                if(checkOver(Wait_Input_Done)) {
                    waitPageError()
                }
                else exec("bs", "\"$phoneNum\"")
            }
            State_WaitScroll -> {
                if (checkOver(Wait_Scroll)) {
                    waitPageError()
                } else exec("kt")
            }
            State_WaitScroll_Click -> {
                if(checkOver(Wait_Scroll_Click)) {
                    receiverClickWaitTime()
                }
            }
            State_WaitValid -> {
                if(checkOver(Wait_Valid)) {
                    numberFailedCount ++
                    waitPageError()
                }
                else exec("valid")
            }
        }
    }


    /// 执行函数设置
    private fun exec(name: String, params: String? = null, code: Int? = callbackCode) {
        when {
            code == null -> loadUrl("javascript:window.CimZzz.$name(${params?:""})")
            params == null -> loadUrl("javascript:window.CimZzz.$name(\"$code\")")
            else -> loadUrl("javascript:window.CimZzz.$name(\"$code\",$params)")
        }
    }

    private fun receiverPostBody(id: String, body: String) {
        client.receiverBody(id, body)
    }

    class JSObject(webView: NewCustomWebView) {
        private val ref = WeakReference(webView)

        private fun checkCode(code: String?): Boolean {
            val view = ref.get()?:return false
            val codeInt = code?.toIntOrNull()?:return false
            return view.callbackCode == codeInt
        }

        @JavascriptInterface
        fun pageSuccess(code: String?, isSuccess: String?) {
//            Log.v("CimZzz", "收到  $isSuccess")
            if(isSuccess == "true")
                SysUtils.runOnMain {
                    if(checkCode(code))
                        ref.get()?.pageLoadSuccess()
                }
        }

        @JavascriptInterface
        fun dialog(code: String?, isSuccess: String?) {
//            Log.v("CimZzz", "收到  $isSuccess")
            if(isSuccess == "true")
                SysUtils.runOnMain {
                    if(checkCode(code))
                        ref.get()?.dialogShowed()
                }
        }

        @JavascriptInterface
        fun input(code: String?, isSuccess: String?) {
//            Log.v("CimZzz", "收到 $isSuccess")
            if(isSuccess == "true")
                SysUtils.runOnMain {
                    if(checkCode(code))
                        ref.get()?.inputSuccess()
                }
        }

        @JavascriptInterface
        fun scroll(code: String?,
                   width: String?,
                   left: String?,
                   top: String?,
                   right: String?,
                   bottom: String?) {
//            Log.v("CimZzz", "收到 $width, $left, $top, $right, $bottom")
            val widthF = width?.toFloatOrNull()?:return
            val leftF = left?.toFloatOrNull()?:return
            val topF = top?.toFloatOrNull()?:return
            val rightF = right?.toFloatOrNull()?:return
            val bottomF = bottom?.toFloatOrNull()?:return

            SysUtils.runOnMain {
                if(checkCode(code))
                    ref.get()?.receiverBound(widthF, leftF, rightF, topF, bottomF)
            }
        }

        @JavascriptInterface
        fun desc(value: String?) {
//            Log.v("CimZzz", "收到 $value")
        }

        @JavascriptInterface
        fun has(code: String?, isSuccess: String?) {
//            Log.v("CimZzz", "验证收到  $isSuccess")
            if(isSuccess == "true")
                SysUtils.runOnMain {
                    if(checkCode(code))
                        ref.get()?.receiverValid()
                }
        }

        @JavascriptInterface
        fun complete(code: String?) {
            SysUtils.runOnMain {
                if(checkCode(code))
                    ref.get()?.receiverComplete()
            }
        }

        @JavascriptInterface
        fun customAjax(id: String?, body: String?) {
            if(id == null || body == null)
                return
            ref.get()?.receiverPostBody(id, body)
        }
    }

}