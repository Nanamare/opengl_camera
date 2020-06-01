package com.pichu.nanamare.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pichu.nanamare.base.ui.BaseViewModel
import com.pichu.nanamare.vm.navigator.MainNavigator

class MainViewModel(private val navigator: MainNavigator) : BaseViewModel(), LifecycleObserver {

    fun onShowCameraFilter() {
        navigator.onShowCameraFilter()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {

    }

}