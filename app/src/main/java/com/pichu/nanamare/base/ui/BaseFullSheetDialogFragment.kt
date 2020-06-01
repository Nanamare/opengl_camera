package com.pichu.nanamare.base.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.pichu.nanamare.R

abstract class BaseFullSheetDialogFragment<B : ViewDataBinding>(private val layoutId: Int) :
    DialogFragment() {

    override fun getTheme(): Int = R.style.FullScreenDialogStyle

    lateinit var binding: B

    var onClickListener: ((position: Int, text: String) -> Unit)? = null

    var onDismissListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this

        requireDialog().window?.requestFeature(Window.FEATURE_NO_TITLE)
        requireDialog().window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), android.R.color.background_light)
        )

        requireDialog().setOnShowListener {
            (it as Dialog).setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onCloseClick()
                    true
                } else {
                    false
                }
            }
        }

        requireDialog().setCanceledOnTouchOutside(false)

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.invoke()
        super.onDismiss(dialog)
    }

    fun onCloseClick() {
        dismiss()
    }

}