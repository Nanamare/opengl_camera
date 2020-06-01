package com.pichu.nanamare.vm

import android.util.SparseArray
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.pichu.nanamare.base.ui.BaseViewModel
import com.pichu.nanamare.camera.filter.shader.CameraFilter
import com.pichu.nanamare.enums.CameraLayoutType
import com.pichu.nanamare.vm.navigator.CameraFilterNavigator

class CameraFilterVieModel(private val navigator: CameraFilterNavigator) :
    BaseViewModel(), LifecycleObserver {

    var liveFilterId = MutableLiveData<Int>().apply { value = 0 }

    val liveCameraFilters =
        MutableLiveData<SparseArray<CameraFilter>>().apply { value = SparseArray() }

    val liveCameraFilter = MutableLiveData<CameraFilter>()

    val liveAvailableResolutions = MutableLiveData<List<String>>()

    val liveSelectResolution =
        MutableLiveData<String>().apply { value = CameraLayoutType.RESOLUTION_9TO16.title }

    fun onTurnOnFlash() {
        navigator.onTurnOnFlash()
    }

    fun onSwitchCamera() {
        navigator.onSwitchCamera()
    }

    fun onChangeLayout(cameraLayoutType: CameraLayoutType) {
        navigator.onChangeLayout(cameraLayoutType)
    }

    fun onShowLayoutPopUp() {
        navigator.onShowLayoutPopUp()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {

    }

}