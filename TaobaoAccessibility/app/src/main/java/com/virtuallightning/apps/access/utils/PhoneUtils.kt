package com.virtuallightning.apps.access.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.virtuallightning.apps.access.bean.ContactBean
import java.nio.file.Files.delete
import android.content.OperationApplicationException
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.content.ContentProviderOperation
import com.virtuallightning.apps.access.accessibility.Constants
import kotlin.random.Random


/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-14 15:32:14
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object PhoneUtils {
    fun removeContact(context: Context) {
        if(SysUtils.checkContactPermission(context)) {
            val uri = Uri.parse("content://com.android.contacts/raw_contacts")
            context.contentResolver.delete(uri, "_id!=-1", null)
        }
    }

    fun writeContact(context: Context, contactBeanList: List<ContactBean>) {
        if(SysUtils.checkContactPermission(context)) {
//            var i = 0
//            val value = ContentValues()
//            for(contactBean in contactBeanList) {
//                value.clear()
//                val rawContactUri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, value)
//                val rawContactId = ContentUris.parseId(rawContactUri)
//                value.clear()
//                value.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
//                value.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                value.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactBean.name)
//                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, value)
//                value.clear()
//                value.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
//                value.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                value.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contactBean.phoneNum)
//                value.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
//                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, value)
//                LogUtils.log("完成第 ${i ++ } 个")
//            }


            val ops = ArrayList<ContentProviderOperation>()
            var rawContactInsertIndex: Int
            for (i in 0 until contactBeanList.size) {
                val bean = contactBeanList[i]
                rawContactInsertIndex = ops.size//这句好很重要，有了它才能给真正的实现批量添加。

                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .withYieldAllowed(true)
                        .build()
                )
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex
                        )
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, bean.name)
                        .withYieldAllowed(true)
                        .build()
                )
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, bean.phoneNum)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_MOBILE)
                        .withYieldAllowed(true)
                        .build()
                )

            }

            try {
                //这里才调用的批量添加
                context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            }
            catch (e: Throwable) {
            }
        }
    }




    fun randomName(): String {
        val nameLength = Random.nextInt(2) + 2
        return buildString {
            for(i in 0 until nameLength)
                append(Constants.NAME[Random.nextInt(Constants.NAME.size)])
        }
    }
}