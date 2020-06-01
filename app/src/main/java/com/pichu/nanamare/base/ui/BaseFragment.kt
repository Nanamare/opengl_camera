package com.pichu.nanamare.base.ui


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pichu.nanamare.base.navigator.BaseNavigator
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding>(private val layoutId: Int) : Fragment(),
    BaseNavigator {

    protected lateinit var binding: B
    protected val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun networkError(errorCode: String) {
        context?.let {
            showToast(errorCode)
        }
    }

    override fun showErrorDialog(errorCode: String) {
        (activity as? BaseActivity<*>)?.showErrorDialog(errorCode)
    }

    override fun showToast(resId: Int) {
        context?.let {
            showToast(it.getString(resId))
        }
    }

    override fun showToast(msg: String) {
        (activity as? BaseActivity<*>)?.showToast(msg)
    }

    fun showKeyboard() {
        Handler().postDelayed({
            (activity as? BaseActivity<*>)?.showKeyboard()
        }, 50)
    }

    override fun hideKeyboard() {
        (activity as? BaseActivity<*>)?.hideKeyboard()
    }

    override fun showLoadingPopup() {
        (activity as? BaseActivity<*>)?.showLoadingPopup()
    }

    override fun hideLoadingPopup() {
        (activity as? BaseActivity<*>)?.hideLoadingPopup()
    }

    override fun onDetach() {
        (activity as? BaseActivity<*>)?.hideLoadingPopup()
        super.onDetach()
    }

}
