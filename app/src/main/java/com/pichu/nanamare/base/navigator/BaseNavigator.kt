package com.pichu.nanamare.base.navigator

interface BaseNavigator {

    fun showLoadingPopup()

    fun hideLoadingPopup()

    fun networkError(errorCode: String = "")

    fun showToast(resId: Int)

    fun showToast(msg: String)

    fun showErrorDialog(errorCode: String = "")

    fun hideKeyboard()

}