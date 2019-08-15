package com.virtuallightning.apps.access.accessibility

import android.os.Environment
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.virtuallightning.apps.access.R
import com.virtuallightning.apps.access.bean.ContactBean
import com.virtuallightning.apps.access.core.BaseAccessibility
import com.virtuallightning.apps.access.core.SubscribeService
import com.virtuallightning.apps.access.utils.PhoneUtils
import com.virtuallightning.apps.access.utils.log
import java.io.*
import java.util.*

class ActionAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    companion object {
        const val STATUS_INIT = 0
        const val STATUS_UNKNOWN = -1
        const val STATUS_PREPARING = 1
        const val STATUS_READY = 2
        const val STATUS_CONTACT = 4
        const val STATUS_CONTACT_COMPLETED = 5

        const val TIME_MOCK_PREPARED = "ACTION_PREPARED"
        const val TIME_NUMBER_PICKER = "ACTION_PICKER"

    }

    private var status = STATUS_INIT
    private var collectionSet = HashSet<ContactBean>()

    private var inputStream: BufferedReader? = null
    private var outputStream: PrintWriter? = null

    override fun onFired() {
        super.onFired()
        status = STATUS_INIT
        tryRecognizePage()
    }

    override fun onHidden() {
        super.onHidden()
        unregisterTimer(TIME_MOCK_PREPARED)
        try {
            inputStream?.close()
        }
        catch (e: Throwable) {

        }
        inputStream = null
        try {
            outputStream?.close()
        }
        catch (e: Throwable) {

        }
        outputStream = null
        status = STATUS_INIT
        collectionSet = HashSet()
    }

    override fun onEvent(event: AccessibilityEvent) {
        when(event.eventType) {
            /// 页面发生变化时
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                when(event.className) {
                    Constants.CNAME.WXACTIVITY -> {
                        when(status) {
                            /// 从后台返回到前台，重新尝试识别页面
                            STATUS_INIT -> {
                                status = STATUS_INIT
                                tryRecognizePage()
                            }
                            /// 从 "手机联系人" 中断回来，直接结束
                            STATUS_CONTACT -> {
                                status = STATUS_INIT
                            }
                            /// 从 "手机联系人" 完成回来，重新尝试识别页面
                            STATUS_CONTACT_COMPLETED -> {
                                status = STATUS_INIT
                                tryRecognizePage()
                            }
                        }
                    }
                    Constants.CNAME.CONTACT -> {
                        when(status) {
                            /// 如果当前准备完毕，去往 "手机联系人" 进行拾取
                            STATUS_READY -> {
                                log("开始拾取手机号")
                                status = STATUS_CONTACT
                                pickCountdownTimer()
                            }
                        }
                    }
                }

                if(status == STATUS_INIT) {
                    /// 当用户在执行流程中作出任何操作导致页面切换都会使整个动作中断
                    log("中断动作")
                    dispose()
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
                    /// 处于手机联系人状态时
                    STATUS_CONTACT -> {
                        /// 开始拾取手机号
                        pickPhoneNum()
                    }
                }
            }
        }
    }


    /**
     * 关闭动作
     * 当页面切换或者异常情况下，会调用该函数
     */
    private fun dispose() {
        log("关闭动作")
        Constants.Client.dispose()
        fireAccessibility(Constants.Accessibility.ENTRY)
    }

    /**
     * 尝试识别当前页面是否为 "通讯录" 页
     */
    private fun tryRecognizePage() {
        log("正在识别")
        if(findViewByText(text= "通讯录") != null) {
            log("已识别，当前页面为通讯录页")
            /// 存在 "通讯录" 三个字，表示当前已经到达通讯录页，切换状态为 "准备中准备下一步动作"
            status = STATUS_PREPARING
            checkPreparing()
        }
    }

    /**
     * 当状态切换至 `Preparing` 时，会检查准备动作
     * 这里应该执行更新联系人目录的操作
     */
    private fun checkPreparing() {
//        Constants.Client.firePreparing()
//        debugPreparing()
        val curInput = inputStream?:{
            val input = getContext().resources.openRawResource(R.raw.phones)
            val reader = BufferedReader(InputStreamReader(input))
            this.inputStream = reader
            reader
        }()

        var idx = 0
        var list = LinkedList<ContactBean>()
        try {
            while (true) {
                val line = curInput.readLine()
                if (line == null) {
                    if (idx == 0) {
                        dispose()
                        return
                    }
                }

                list.add(ContactBean(idx.toString(), line))
                idx++

                if (idx >= 10)
                    break
            }

            PhoneUtils.removeContact(getContext())
            PhoneUtils.writeContact(getContext(), list)
            log("写入手机号完成")
            readyForWorking()
        }
        catch (e: Throwable) {
            dispose()
        }
    }

    /**
     * 用于 debug 的准备工作
     */
    private fun debugPreparing() {
        log("开始执行 debug 专用准备")
//        registerTimer(TIME_MOCK_PREPARED, 5000L, isOnce = true) {
//            /// 执行完成准备工作
//            readyForWorking()
//        }
    }

    /**
     * 当准备工作完成时，表示已经可以开始执行工作了
     */
    private fun readyForWorking() {
        if(status == STATUS_PREPARING) {
            val node = findViewByDescription(description = "手机联系人")
            if(node == null || node.parent == null) {
                /// 不存在该按钮的情况（一般不会发生，除非淘宝改版）
                dispose()
                return
            }
            status = STATUS_READY
            log("执行点击事件")
            node.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    private fun pickCountdownTimer() {
        registerTimer(TIME_NUMBER_PICKER, 3000L, 0L, isOnce = true) {
            completed()
        }
    }

    /**
     * 开始拾取手机号
     */
    private fun pickPhoneNum() {
        if(status == STATUS_CONTACT) {
            /// 首先找到 ListView
            val listViewNode = findViewByFirstClassName(clsName = Constants.CNAME.LIST_VIEW)
            if(listViewNode == null) {
                /// 不存在列表的情况（一般不会发生，除非淘宝改版）
                dispose()
                return
            }

            /// 遍历列表中全部元素放入到号码集合中
            (0 until listViewNode.childCount).forEach {
                /// 获取列表项节点，目前版本，如果子项存在三个子节点表示正确的用户节点。
                val itemNode = listViewNode.getChild(it)
                if(itemNode.childCount == 3) {
                    /// 第一个子项是用户名
                    /// 第二个子项是手机号
                    /// 第三个子项是按钮
                    val nameNode = itemNode.getChild(0)
                    val numberNode = itemNode.getChild(1)

                    /// 用户名和手机号应均不为 `null`
                    val name = nameNode.text?:return@forEach
                    val number = numberNode.text?:return@forEach

                    collectionSet.add(ContactBean(name.toString(), number.toString()))
                }
            }

            /// 添加完执行滚动逻辑
            listViewNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            pickCountdownTimer()
        }
    }

    /**
     * 完成拾取工作
     */
    private fun completed() {
        log("完成")
        if(status == STATUS_CONTACT) {
            val node = findViewByDescription(description = "转到上一层级")
            if(node == null) {
                /// 不存在该按钮的情况（一般不会发生，除非淘宝改版）
                dispose()
                return
            }

            handleData()
            collectionSet = HashSet()
            status = STATUS_CONTACT_COMPLETED
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }


    private var file: File = File(Environment.getExternalStorageDirectory(), "valid.txt")
    /**
     * 处理拾取到的手机联系人数据，处理结束后该集合将置为 null
     */
    private fun handleData() {
        log("有效手机号: $collectionSet")
        val curOutput = outputStream?:{
            val stream = FileOutputStream(file)
            val writer = PrintWriter(stream)
            outputStream = writer
            writer
        }()

        collectionSet.forEach {
            curOutput.println(it)
        }
        curOutput.flush()
    }
}