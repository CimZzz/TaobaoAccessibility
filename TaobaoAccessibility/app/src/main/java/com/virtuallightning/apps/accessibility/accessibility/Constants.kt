package com.virtuallightning.apps.accessibility.accessibility

import com.virtuallightning.apps.accessibility.core.SubscribeService

object Constants {

    object CNAME {
        const val WXACTIVITY = "com.taobao.weex.WXActivity"
        const val CONTACT = "com.taobao.message.activity.IMContactsListActivity"
    }

    object Accessibility {
        lateinit var ENTRY: EntryAccessibility
        lateinit var ACTION: ActionAccessibility
    }


    fun init(service: SubscribeService) {
        Accessibility.ENTRY = EntryAccessibility(service)
        Accessibility.ACTION = ActionAccessibility(service)
    }
}