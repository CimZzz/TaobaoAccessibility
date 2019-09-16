package com.virtualightning.webview.core

import okhttp3.Response
import java.lang.ref.WeakReference

typealias TimerCallback = (Boolean) -> Unit

typealias PickerCallback<T> = (T) -> Unit

typealias RegistryCallback = () -> Unit

typealias TouchOverCallback = () -> Unit

typealias GetResultNotNull<T> = () -> T

typealias RequestParser<T> = (Response) -> T?
typealias RequestCallback<T> = (Boolean, T?) -> Unit
typealias RefRequestCallback<E, T> = (WeakReference<E>, Boolean, T?) -> Unit