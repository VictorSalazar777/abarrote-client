package com.manuelsoft.app

import android.text.InputFilter
import android.text.Spanned

class InputFilterDecimal : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val input = dest.toString() + source.toString()
        if (input == ".") {
            return "0."
        }
        if (input == "0" && source != "0") {
            return ""
        }
        if (!input.matches(Regex("^\\d{0,6}(\\.\\d{0,2})?$"))) {
            return ""
        }
        return null
    }
}
