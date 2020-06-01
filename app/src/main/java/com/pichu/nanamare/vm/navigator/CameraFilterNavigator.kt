package com.pichu.nanamare.vm.navigator

import com.pichu.nanamare.enums.CameraLayoutType

interface CameraFilterNavigator {
    fun onTurnOnFlash()
    fun onSwitchCamera()
    fun onChangeLayout(cameraLayoutType: CameraLayoutType)
    fun onShowLayoutPopUp()
}