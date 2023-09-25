package com.manuelsoft.app

import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned


class MoneyValueFilter : InputFilter {

    private val digits = 2

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {

        val len: Int = end - start;

        // if deleting, source is empty
        // and deleting can't break anything
        if (len == 0) {
            return source.toString()
        }

        val dlen = dest?.length

        // Find the position of the decimal .
        for (i in 0 until dstart) {
            if (dest?.get(i) == '.') {
                // being here means, that a number has
                // been inserted after the dot
                // check if the amount of digits is right
                if (dlen != null) {
                    return if ((dlen - (i + 1) + len > digits)) "" else SpannableStringBuilder(
                        source,
                        start,
                        end
                    )
                }
            }
        }

        for (i in start until end) {
            if (source?.get(i) == '.') {
                // being here means, dot has been inserted
                // check if the amount of digits is right
                if (dlen != null) {
                    if ((dlen - dend) + (end - (i + 1)) > digits)
                        return ""
                    else
                        break
                }  // return new SpannableStringBuilder(source, start, end);
            }
        }

        // if the dot is after the inserted part,
        // nothing can break
        return SpannableStringBuilder(source, start, end)

    }


}