package com.virtuallightning.apps.access.accessibility

import android.view.accessibility.AccessibilityEvent
import com.virtuallightning.apps.access.core.BaseAccessibility
import com.virtuallightning.apps.access.core.SubscribeService
import com.virtuallightning.apps.access.utils.log

class EntryAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    override fun onFired() {
        super.onFired()
        log("切换至 Entry")
    }

    override fun onHidden() {
        super.onHidden()
        log("离开 Entry")
    }

    override fun onEvent(event: AccessibilityEvent) {
        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if(event.className == Constants.CNAME.WXACTIVITY) {
                    fireAccessibility(Constants.Accessibility.ACTION)
                }
                else log(event.className)
            }
        }
    }
}