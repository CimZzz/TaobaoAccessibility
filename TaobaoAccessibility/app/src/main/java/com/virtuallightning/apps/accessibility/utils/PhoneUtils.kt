package com.virtuallightning.apps.accessibility.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.virtuallightning.apps.accessibility.bean.ContactBean

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-14 15:32:14
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object PhoneUtils {
    fun writeContact(context: Context, contactBeanList: List<ContactBean>) {
        if(SysUtils.checkContactPermission(context)) {
            val value = ContentValues()
            for(contactBean in contactBeanList) {
                value.clear()
                val rawContactUri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, value)
                val rawContactId = ContentUris.parseId(rawContactUri)
                value.clear()
                value.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                value.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                value.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactBean.name)
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, value)
                value.clear()
                value.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                value.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                value.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contactBean.phoneNum)
                value.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, value)
            }
        }
    }
}