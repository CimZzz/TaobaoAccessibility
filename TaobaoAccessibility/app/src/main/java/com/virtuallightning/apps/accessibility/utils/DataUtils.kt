package com.virtuallightning.apps.accessibility.utils

import android.content.Context
import android.content.SharedPreferences


object DataUtils {
    var sp: SharedPreferences? = null
    fun init(context: Context) {
        if(sp == null)
            sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    }
}