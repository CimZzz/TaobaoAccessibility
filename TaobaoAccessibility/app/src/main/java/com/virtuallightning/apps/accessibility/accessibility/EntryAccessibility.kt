package com.virtuallightning.apps.accessibility.accessibility

import android.view.accessibility.AccessibilityEvent
import com.virtuallightning.apps.accessibility.core.BaseAccessibility
import com.virtuallightning.apps.accessibility.core.SubscribeService

class EntryAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    override fun onEvent(event: AccessibilityEvent) {
        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if(event.className == Constants.CNAME.WXACTIVITY) {
                    fireAccessibility(Constants.Accessibility.ACTION)
                }
            }
        }
    }
}