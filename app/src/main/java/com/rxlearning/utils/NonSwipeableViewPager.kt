package com.rxlearning.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipeableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    internal var enabled = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) performClick()
        return if (this.enabled) super.onTouchEvent(event) else false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onInterceptTouchEvent(event: MotionEvent) = if (this.enabled) super.onInterceptTouchEvent(event) else false

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}