package com.virtuallightning.apps.access.accessibility

import com.virtuallightning.apps.access.client.Client
import com.virtuallightning.apps.access.core.SubscribeService

object Constants {

    object CNAME {
        const val WXACTIVITY = "com.taobao.weex.WXActivity"
        const val CONTACT = "com.taobao.message.activity.IMContactsListActivity"
        const val LIST_VIEW = "android.widget.ListView"
    }

    object Accessibility {
        lateinit var ENTRY: EntryAccessibility
        lateinit var ACTION: ActionAccessibility
    }

    lateinit var Client: Client


    fun init(service: SubscribeService) {
        Accessibility.ENTRY = EntryAccessibility(service)
        Accessibility.ACTION = ActionAccessibility(service)
        Client = Client(service)
    }

    fun dispose() {
        Accessibility.ENTRY.onHidden()
        Accessibility.ACTION.onHidden()
    }
}