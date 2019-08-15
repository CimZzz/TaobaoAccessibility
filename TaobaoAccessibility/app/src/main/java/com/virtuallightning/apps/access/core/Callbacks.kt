package com.virtuallightning.apps.access.core


typealias TravelCallback<T> = (T) -> Boolean

typealias TimerCallback = (Boolean) -> Unit

typealias GetResultNotNull<T> = () -> T