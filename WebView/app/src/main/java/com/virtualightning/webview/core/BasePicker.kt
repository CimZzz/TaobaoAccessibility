package com.virtualightning.webview.core

import android.os.Looper
import android.support.v4.util.SparseArrayCompat
import java.util.concurrent.ConcurrentLinkedQueue

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 11:54:07
 *  Project : WebView
 *  Since Version : Alpha
 */

abstract class BasePicker<T> {
    private var isDoAction = false
    private val queue = ConcurrentLinkedQueue<T>()
    private val callbackMap = SparseArrayCompat<PickerCallback<T>>()

    fun register(id: Int, callback: PickerCallback<T>) {
        val item = queue.poll()
        if(item != null) {
            callback(item)
            return
        }
        callbackMap.put(id, callback)
        action()
    }

    fun unregister(id: Int) {
        callbackMap.remove(id)
    }

    protected fun completeAction(collection: Collection<T>) {
        if(Looper.myLooper() != Looper.getMainLooper())
            SysUtils.runOnMain {
                actionDone(collection)
            }
        else actionDone(collection)
    }

    private fun actionDone(collection: Collection<T>) {
        isDoAction = false
        queue.addAll(collection)
        while (queue.isNotEmpty()) {
            if(callbackMap.size() == 0)
                break
            val item = queue.poll() ?: break
            val key = callbackMap.keyAt(0)
            val value = callbackMap.valueAt(0)
            callbackMap.remove(key)
            value(item)
        }
    }

    private fun action() {
        if(!isDoAction) {
            isDoAction = true
            onAction()
        }
    }

    protected abstract fun onAction()
}