package com.virtuallightning.apps.access.utils

import android.content.Context
import android.content.SharedPreferences
import com.virtuallightning.apps.access.utils.spdelegate.StringSPDelegate


object DataUtils {
    private lateinit var sp: SharedPreferences


    fun init(context: Context) {
        sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    }
}