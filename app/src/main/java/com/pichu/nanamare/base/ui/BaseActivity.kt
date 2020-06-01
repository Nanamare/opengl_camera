package com.pichu.nanamare.base.ui

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.pichu.nanamare.R
import com.pichu.nanamare.base.navigator.BaseNavigator
import com.pichu.nanamare.custom.LoadingDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar_back_title.*


abstract class BaseActivity<B : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity(),
    BaseNavigator {

    protected lateinit var binding: B
    protected val compositeDisposable = CompositeDisposable()
    private var loadingPopup: LoadingDialog? = null
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        iv_back?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun setToolbarTitle(title: String) {
        tv_title?.text = title
    }

    fun setToolbarTitle(strRes: Int) {
        tv_title?.text = getString(strRes)
    }

    fun hideToolbarBackIcon() {
        iv_back?.visibility = View.INVISIBLE
    }

    fun showToolbarBackIcon() {
        iv_back?.visibility = View.VISIBLE
    }

    override fun networkError(errorCode: String) {

    }

    override fun showErrorDialog(errorCode: String) {

    }

    override fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    override fun showToast(msg: String) {
        if (toast != null) {
            toast?.cancel()
        }
        val layout = layoutInflater.inflate(R.layout.custom_toast, binding.root as ViewGroup, false)
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        layout.findViewById<TextView>(R.id.tv_toast_text).run {
            text = msg
            width = size.x
        }
        Handler(Looper.getMainLooper()).apply {
            post {
                toast = Toast(applicationContext).apply {
                    setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
                    duration = Toast.LENGTH_SHORT
                    view = layout
                }
                toast?.show()
            }
        }
    }

    fun showKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    override fun hideKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            (currentFocus
                ?: View(this)).windowToken, 0
        )
    }

    override fun showLoadingPopup() {
        if (loadingPopup == null) {
            loadingPopup = LoadingDialog.newInstance().apply {
                isCancelable = false
                show(supportFragmentManager, loadingPopup)
            }
        }
    }

    override fun hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup?.dismissAllowingStateLoss()
            loadingPopup = null
        }
    }

    companion object {
        @JvmStatic
        val TAG: String = BaseActivity::class.java.simpleName
    }
}
