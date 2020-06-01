package com.pichu.nanamare.ext

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@BindingAdapter(value = ["decimalTextAttrChanged"])
fun EditText.setDecimalTextListener(listener: InverseBindingListener) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

@BindingAdapter(value = ["decimalText"])
fun EditText.setIntValue(value: Int?) {
    val decimalFormat = DecimalFormat("#,##0")
    val nonNullValue = value ?: 0

    val strValue = decimalFormat.format(nonNullValue)
    if (text.toString() == strValue) {
        return
    }

    var selectionPosition = selectionEnd + (strValue.length - text.length)
    if (selectionPosition < 0) {
        selectionPosition = 1
    }
    setText(strValue)
    setSelection(selectionPosition)
}


@InverseBindingAdapter(attribute = "decimalText")
fun EditText.getIntValue(): Int {
    val groupingSeparator = DecimalFormatSymbols.getInstance().groupingSeparator
    return text.toString().replace(Regex("[$groupingSeparator]"), "").let {
        if (it.isEmpty()) {
            0
        } else {
            try {
                return@let it.toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                return@let 0
            }
        }
    }
}

@BindingAdapter(value = ["doubleTextAttrChanged"])
fun EditText.setDoubleTextListener(listener: InverseBindingListener) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

val doubleFormat = DecimalFormat("#,##0.##")

@BindingAdapter(value = ["doubleText"])
fun EditText.setDoubleValue(value: Double?) {
    val nonNullValue = value ?: .0

    val strValue = doubleFormat.format(nonNullValue)
    if (text.toString() == strValue) {
        return
    }

    var selectionPosition = selectionEnd + (strValue.length - text.length)
    if (selectionPosition < 0) {
        selectionPosition = 1
    }
    setText(strValue)
    setSelection(selectionPosition)
}

@InverseBindingAdapter(attribute = "doubleText")
fun EditText.getDoubleValue(): Double {
    return text.toString().replace(",", "").let {
        if (it.isEmpty()) {
            .0
        } else {
            try {
                return@let it.toDouble()
            } catch (e: Exception) {
                return@let .0
            }
        }
    }
}

// TODO Add binding adapter
fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}
