package com.virtuallightning.apps.accessibility.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-14 15:37:23
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object SysUtils {
    private val contactPermission = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)

    fun checkContactPermission(context: Context): Boolean {
        return checkPermission(context, contactPermission)
    }

    fun checkPermission(context: Context, permissions: Array<String>): Boolean {
        var isGrant = true
        for(permission in permissions) {
            isGrant = ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_GRANTED
            if(!isGrant)
                break
        }

        return isGrant
    }
}