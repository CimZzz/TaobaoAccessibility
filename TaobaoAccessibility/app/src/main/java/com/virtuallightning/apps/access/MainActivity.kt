package com.virtuallightning.apps.access

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.virtuallightning.apps.access.bean.ContactBean
import com.virtuallightning.apps.access.utils.DataUtils
import com.virtuallightning.apps.access.utils.PhoneUtils
import com.virtuallightning.apps.access.utils.SysUtils
import com.virtuallightning.apps.access.utils.log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataUtils.init(this)
        openBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        clearBtn.setOnClickListener {
            log("清空联系人")
            PhoneUtils.removeContact(this@MainActivity)
            log("清空联系人 完成")
        }

        readPermissionBtn.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }

        writePermissionBtn.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS), 100)
        }
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }

    override fun onResume() {
        super.onResume()

        if(SysUtils.checkPermission(this, arrayOf(Manifest.permission.READ_CONTACTS))) {
            readPermissionBtn.visibility = View.GONE
            readPermissionTxt.visibility = View.VISIBLE
        }
        else {
            readPermissionBtn.visibility = View.VISIBLE
            readPermissionTxt.visibility = View.GONE
        }

        if(SysUtils.checkPermission(this, arrayOf(Manifest.permission.WRITE_CONTACTS))) {
            writePermissionBtn.visibility = View.GONE
            writePermissionTxt.visibility = View.VISIBLE
        }
        else {
            writePermissionBtn.visibility = View.VISIBLE
            writePermissionTxt.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100) {
            (0 until permissions.size).forEach {
                val permission = permissions[it]
                val result = grantResults[it]
                when(permission) {
                    Manifest.permission.READ_CONTACTS -> {
                        if(result == PackageManager.PERMISSION_GRANTED) {
                            readPermissionBtn.visibility = View.GONE
                            readPermissionTxt.visibility = View.VISIBLE
                        }
                    }

                    Manifest.permission.WRITE_CONTACTS -> {
                        if(result == PackageManager.PERMISSION_GRANTED) {
                            writePermissionBtn.visibility = View.GONE
                            writePermissionTxt.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}
