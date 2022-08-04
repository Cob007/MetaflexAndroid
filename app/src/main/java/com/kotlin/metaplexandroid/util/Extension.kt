package com.kotlin.metaplexandroid.util

import com.kotlin.metaplexandroid.R

fun String?.verifyAsString(): Int? {
    return if (this.isNullOrEmpty()) {
        R.string.empty_error
    } else {
        null
    }
}