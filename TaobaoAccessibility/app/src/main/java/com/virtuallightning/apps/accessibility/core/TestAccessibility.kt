package com.virtuallightning.apps.accessibility.core

import android.view.accessibility.AccessibilityEvent


class TestAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    override fun onEvent(event: AccessibilityEvent) {

        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                log("Window state changed: 222${event.className}")
            }
        }
    }
}