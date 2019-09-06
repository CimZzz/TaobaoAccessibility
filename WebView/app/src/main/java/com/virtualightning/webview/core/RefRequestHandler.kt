package com.virtualightning.webview.core

import java.lang.ref.WeakReference

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 08:23:12
 *  Project : WebView
 *  Since Version : Alpha
 */
class RefRequestHandler<E, T>(
    obj: E,
    private val callback: RefRequestCallback<E, T>
): RequestCallback<T> {
    private val objRef = WeakReference(obj)

    override fun invoke(success: Boolean, resp : T?) {
        callback.invoke(objRef, success, resp)
    }
}