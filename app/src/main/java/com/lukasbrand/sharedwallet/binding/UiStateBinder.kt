package com.lukasbrand.sharedwallet.binding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.UiState

object UiStateBinder {

    @BindingAdapter("uiStateStringAttrChanged")
    @JvmStatic
    fun setStateUiStringListener(editText: EditText, listener: InverseBindingListener) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                listener.onChange()
            }
        })
    }

    @BindingAdapter("uiStateString")
    @JvmStatic
    fun setUiStateString(view: EditText, value: UiState<String>) {
        when (value) {
            is UiState.Set -> {
                if (view.text.toString() != value.data) {
                    view.setText(value.data)
                } else {
                    //text is already equal, avoiding loop
                    return
                }
            }
            UiState.Unset -> {
                if (view.text.toString().isNotEmpty()) {
                    view.setText("")
                } else {
                    //if text is already empty we do not need to overwrite it
                    return
                }
            }
        }.exhaustive
    }

    @InverseBindingAdapter(attribute = "uiStateString", event = "uiStateStringAttrChanged")
    @JvmStatic
    fun getUiStateString(editText: EditText): UiState<String> {
        return if (editText.text.toString().isNotBlank()) {
            UiState.Set(editText.text.toString())
        } else {
            UiState.Unset
        }
    }
}
