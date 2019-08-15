package com.virtuallightning.apps.accessibility.accessibility

import android.view.accessibility.AccessibilityEvent
import com.virtuallightning.apps.accessibility.core.BaseAccessibility
import com.virtuallightning.apps.accessibility.core.SubscribeService
import com.virtuallightning.apps.accessibility.core.log

class ActionAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    companion object {
        const val STATUS_INIT = 0
        const val STATUS_RECOGNIZED = 1
    }

    private var status = STATUS_INIT

    override fun onFired() {
        super.onFired()
        status = STATUS_INIT
    }

    override fun onEvent(event: AccessibilityEvent) {
        when(event.eventType) {
            /// 页面发生变化时
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                when(status) {

                }
            }

            /// 内容发生变化时
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                when(status) {
                    /// 仍处于初始化状态时
                    STATUS_INIT -> {
                        /// 尝试去识别是否当前页面为 "通讯录"
                        tryRecognizePage()
                    }
                }
            }
        }
    }

    private fun handlePageTransfer() {

    }

    private fun tryRecognizePage() {
        log("正在识别")
        log(convertJSON().toString())
        status = if(findViewByDescription(description = "通讯录") != null) {
            log("已识别，当前页面为通讯录页")
            /// 存在 "通讯录" 三个字，表示当前已经到达通讯录页，切换状态为 "已识别"
            STATUS_RECOGNIZED
        } else STATUS_INIT
    }
}