package com.kotlin.metaplexandroid.util

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

@BindingAdapter("showText")
fun showText(textView: TextView, @StringRes res: Int?) {
    if (res == null) {
        textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
        textView.text = textView.resources.getText(res)
    }
}