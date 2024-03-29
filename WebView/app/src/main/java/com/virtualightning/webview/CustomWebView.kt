package com.virtualightning.webview

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.virtualightning.webview.core.*
import java.lang.ref.WeakReference

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 00:07:31
 *  Project : WebView
 *  Since Version : Alpha
 */
class CustomWebView: WebView {
    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)

    private val targetUrl = "https://ai.m.taobao.com/index.html?pid=mm_229720104_254050230_70587600194"


    init {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = false
        settings.blockNetworkImage = true
        settings.useWideViewPort = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webViewClient = WebClient(this)
        addJavascriptInterface(JSObject(this), "tskcj")
        ConfigRegistry.register(::receiverConfig)
    }

    companion object {
        private const val State_Init = 0
        private const val State_WaitScript = 1
        private const val State_WaitPhoneNum = 2
        private const val State_WaitConfig = 3
        private const val State_WaitUserAgent = 4
        private const val State_WaitPage = 5
        private const val State_WaitPage_Transfer_Proxy = 6
        private const val State_WaitDialog = 7
        private const val State_WaitInput = 8
        private const val State_WaitInput_Done = 9
        private const val State_WaitScroll = 10

        /// 重试次数
        private const val Error_Page = 0


        /// 最多等待 10 秒
        private const val Wait_Page = 20000L
        private const val Wait_Page_Transfer_Proxy = 3000L
        private const val Wait_Dialog = 4000L
        private const val Wait_Input = 1000L
        private const val Wait_Input_Done = 1000L
        private const val Wait_Scroll = 8000L
    }

    private var state: Int = 0
    private var stateTime: Long = 0L
    private var script: String = ""
    private var phoneNum: String = ""
    private var phaseName: String = ""
    private var userAgent: String = ""
    private var isScriptSuccess: Boolean = false
    private var isPageSuccess: Boolean = false
    private var errorCount = 0
    private var callbackCode: Int = 0

    private var contentWidth: Float = 0f
    private var orgScrollLeft: Float = 0f
    private var orgScrollTop: Float = 0f
    private var orgScrollRight: Float = 0f
    private var orgScrollBottom: Float = 0f
    private var scrollStartX: Float = 0f
    private var scrollEndX: Float = 0f
    private var scrollY: Float = 0f
    private var viewTouchBundle: ViewTouchBundle? = null
    private var isScrollOver: Boolean = false

    private var pickerSet = HashSet<BasePicker<*>>()
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
        isScriptSuccess = false
        isPageSuccess = false
        errorCount = 0
        increaseCode()
        viewTouchBundle?.close()
        viewTouchBundle = null
        isScrollOver = false
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
        changePhase("脚本")
        if(script.isNotEmpty())
            receiverScript(script)
        else registerPicker(ScriptPicker, ::receiverScript)
    }

    private fun receiverScript(script: String) {
        this.script = script
        logPhase("已获取脚本")
        unregisterPicker(ScriptPicker)
        waitNumber()
    }

    /// 阶段-2 获取手机号码
    private fun waitNumber() {
        state = State_WaitPhoneNum
        changePhase("号码")
        registerPicker(PhoneNumberPicker, ::receiverNum)
    }

    private fun receiverNum(num: String) {
        phoneNum = num
        logPhase("收到号码: $num")
        unregisterPicker(PhoneNumberPicker)
        waitConfig()
    }


    /// 阶段-3 等待
    private fun waitConfig() {
        state = State_WaitConfig
        increaseCode()
        SysUtils.unregisterTimerCallback("timer$viewId")
        stopLoading()
//        clearCache(true)
//        clearHistory()
        changePhase("检查配置")
        ConfigRegistry.waitConfig()
    }

    private fun receiverConfig() {
        logPhase("配置完成")
        waitUserAgent()
    }

    /// 阶段-3 获取 UA
    private fun waitUserAgent() {
        state = State_WaitUserAgent
        changePhase("UserAgent")
        registerPicker(UserAgentPicker, ::receiverUserAgent)
    }

    private fun receiverUserAgent(userAgent: String) {
        this.userAgent = userAgent
        logPhase("收到UA: $userAgent")
        unregisterPicker(UserAgentPicker)
        settings.userAgentString = userAgent
        waitPage()
    }


    /// 阶段-4 开始加载网页
    /// 同时开始打开计时器
    private fun waitPage() {
        if(state != State_WaitPage) {
            state = State_WaitPage
            changePhase("加载页面")
        }
        increaseCode()
        resetStateTime()
        isScriptSuccess = false
        isPageSuccess = false

        contentWidth = 0f
        orgScrollLeft = 0f
        orgScrollRight = 0f
        scrollStartX = 0f
        scrollEndX = 0f
        scrollY = 0f
        viewTouchBundle?.close()
        viewTouchBundle = null
        isScrollOver = false

        SysUtils.unregisterTimerCallback("timer$viewId")
        SysUtils.registerTimerCallback("timer$viewId", 200L, isOnce = false) {
            checkStateTimer()
        }
        loadUrl(targetUrl)
    }

    /// 页面加载失败
    /// 后续弹窗，输入失败均调用此方法重试
    private fun waitPageError() {
        errorCount ++
        if(errorCount > Error_Page) {
            viewTouchBundle?.close()
            viewTouchBundle = null
            increaseCode()
            state = State_WaitPage_Transfer_Proxy
            errorCount = 0
            logPhase("页面超过 $Error_Page 次重试失败，将在 ${Wait_Page_Transfer_Proxy / 1000} 秒后重新获取代理")
        }
        else {
            logPhase("第 $errorCount 次失败，正在重试...")
            waitPage()
        }
    }

    /// 获取脚本成功
    private fun pageInjectSuccess() {
        if(state != State_WaitPage)
            return
        isScriptSuccess = true
        logPhase("已注入脚本")
        checkPageSuccess()
    }

    private fun pageLoadSuccess() {
        if(state != State_WaitPage)
            return
        isPageSuccess = true
        logPhase("已加载页面")
        checkPageSuccess()
    }

    /// 判断页面是否加载成功
    private fun checkPageSuccess() {
        if(!isScriptSuccess || !isPageSuccess || state != State_WaitPage)
            return

        logPhase("页面已就绪")
        waitDialog()
    }

    /// 阶段-5 开始显示弹窗
    /// 弹窗是通过点击红包按钮或者自动显示的
    private fun waitDialog() {
        state = State_WaitDialog
        changePhase("弹窗")
        increaseCode()
        resetStateTime()
        exec("xc", "\"true\"")
    }

    private fun dialogShowed() {
        if(state != State_WaitDialog)
            return

        logPhase("成功显示弹窗")
        waitInput()
    }

    /// 阶段-6 输入号码
    private fun waitInput() {
        state = State_WaitInput
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
        logPhase("输入完成")
        waitScroll()
    }

    /// 阶段-7 滑动滑块
    /// 滑块大约有 8 秒的等待时间，当第一次获取 bound 时开始滑动
    /// 当布局发生变动时，重新开始滑动并且重置时间
    private fun waitScroll() {
        state = State_WaitScroll
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
        if(isScrollOver)
            return

        if(contentWidth == 0f)
            return

        if(this.contentWidth == contentWidth &&
            this.orgScrollLeft == orgViewLeft &&
            this.orgScrollRight == orgViewRight &&
            this.orgScrollTop == orgViewTop &&
            this.orgScrollBottom == orgViewBottom)
            return

        this.viewTouchBundle?.close()
        this.viewTouchBundle = null
        this.contentWidth = contentWidth
        this.orgScrollLeft = orgViewLeft
        this.orgScrollRight = orgViewRight
        this.orgScrollTop = orgViewTop
        this.orgScrollBottom = orgViewBottom

        val ratio = width / this.contentWidth
        val buttonHalfHeight = (orgViewBottom - orgViewTop) / 2

        this.scrollStartX = (orgViewLeft + buttonHalfHeight) * ratio
        this.scrollEndX = (orgViewRight - buttonHalfHeight) * ratio

        this.scrollY = (this.orgScrollTop + buttonHalfHeight) * ratio
        logPhase("位置变化重置滑动")
        increaseCode()
        resetStateTime()
        exec("mcl")
//        val bundle = ViewTouchBundle(mockTouch(this), scrollStartX, scrollEndX, scrollY, ::scrollCompleted)
//        this.viewTouchBundle = bundle
//        TouchUtils.addBundle(bundle = bundle)
    }

    private fun scrollCompleted() {
        isScrollOver = true
        logPhase("完成滑动，等待时间结束")
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
                    waitConfig()
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
                if(checkOver(Wait_Scroll)) {
                    if(isScrollOver) {
                        /// 完成滚动
                        logPhase("完成一次输入")
                        waitNumber()
                    }
                    else waitPageError()
                }
                else exec("kt")
            }
        }
    }


    /// 执行函数设置
    private fun exec(name: String, params: String? = null, code: Int? = callbackCode) {
        if(isScriptSuccess) {
            when {
                code == null -> loadUrl("javascript:window.CimZzz.$name(${params?:""})")
                params == null -> loadUrl("javascript:window.CimZzz.$name(\"$code\")")
                else -> loadUrl("javascript:window.CimZzz.$name(\"$code\",$params)")
            }
        }
    }

    class WebClient(webView: CustomWebView): WebViewClient() {
        private val ref = WeakReference(webView)

        override fun onPageFinished(p0: WebView?, p1: String?) {
            super.onPageFinished(p0, p1)
            SysUtils.runOnMain {
                val webView = ref.get()
                if(webView != null) {
                    webView.loadUrl("javascript:${webView.script}")
                    webView.pageInjectSuccess()
                }
            }
        }
    }

    class JSObject(webView: CustomWebView) {
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
    }
}